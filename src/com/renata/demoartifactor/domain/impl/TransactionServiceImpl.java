package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.TransactionService;
import com.renata.demoartifactor.domain.dto.TransactionAddDto;
import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import com.renata.demoartifactor.persistance.repository.contracts.TransactionRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

final class TransactionServiceImpl extends GenericService<Transaction> implements
    TransactionService {

    private final TransactionRepository transactionRepository;

    TransactionServiceImpl(TransactionRepository transactionRepository) {
        super(transactionRepository);
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Set<Transaction> getAllByDate(LocalDate date) {
        return new TreeSet<>(transactionRepository.findByDate(date));
    }

    @Override
    public Set<Transaction> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<Transaction> getAll(Predicate<Transaction> filter) {
        return new TreeSet<>(transactionRepository.findAll(filter));
    }

    @Override
    public Transaction add(TransactionAddDto transactionAddDto) {

        Transaction transaction = new Transaction(
            transactionAddDto.getId(),
            transactionAddDto.type(),
            transactionAddDto.date(),
            transactionAddDto.item(),
            transactionAddDto.value(),
            transactionAddDto.user()
        );

        transactionRepository.add(transaction);
        generateReport(t -> true);
        return transaction;
    }

    @Override
    public void generateReport(Predicate<Transaction> filter) {
        File reportsDir = new File(REPORTS_DIRECTORY);
        if (!reportsDir.exists() && !reportsDir.mkdirs()) {
            throw new RuntimeException("Не вдалося створити директорію: " + REPORTS_DIRECTORY);
        }

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"№", "ID Транзакції", "Тип", "Дата", "ID Товару", "Товар", "Сума",
            "ID Користувача", "Користувач"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        Set<Transaction> transactions = getAll(filter);

        int index = 1;
        for (Transaction transaction : transactions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(index++);
            row.createCell(1).setCellValue(transaction.getId().toString());
            row.createCell(2).setCellValue(transaction.getType().getName());
            row.createCell(3).setCellValue(transaction.getFormattedDate());
            row.createCell(4).setCellValue(transaction.getItem().getId().toString());
            row.createCell(5).setCellValue(transaction.getItem().getName());
            row.createCell(6).setCellValue(transaction.getValue());
            row.createCell(7).setCellValue(transaction.getUser().getId().toString());
            row.createCell(8).setCellValue(transaction.getUser().getUsername());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        String fileName = "transactions[%s].xls".formatted(LocalDateTime.now().toString())
            .replace(':', '-');

        Path outputPath = Path.of(REPORTS_DIRECTORY,
            fileName);
        try (FileOutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Помилка при збереженні звіту користувачів: %s"
                .formatted(e.getMessage()));
        }
    }
}
