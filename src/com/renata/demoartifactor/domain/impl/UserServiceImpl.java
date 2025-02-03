package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.UserService;
import com.renata.demoartifactor.domain.dto.UserAddDto;
import com.renata.demoartifactor.domain.exception.EntityNotFoundException;
import com.renata.demoartifactor.domain.exception.SignUpException;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mindrot.bcrypt.BCrypt;

final class UserServiceImpl extends GenericService<User> implements UserService {

    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Такої пошти користувача не існує."));
    }

    @Override
    public Set<User> getAll() {
        return getAll(u -> true);
    }

    @Override
    public Set<User> getAll(Predicate<User> filter) {
        return new TreeSet<>(userRepository.findAll(filter));
    }

    @Override
    public User add(UserAddDto userAddDto) {
        var user = new User(userAddDto.getId(),
            BCrypt.hashpw(userAddDto.rawPassword(), BCrypt.gensalt()),
            userAddDto.email(),
            userAddDto.username(),
            userAddDto.role());
        userRepository.add(user);
        return user;
    }

    // consider removing this
    //TODO instead add xls file for TransactionHistory
    @Override
    public void generateReport(Predicate<User> filter) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"№", "Логін", "Email", "Role"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (User user : getAll(filter)) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(user.getUsername());
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(user.getRole().getName());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        String fileName = "users[%s].xls".formatted(LocalDateTime.now().toString())
            .replace(':', '-');

        Path outputPath = Path.of(REPORTS_DIRECTORY,
            fileName);
        try (FileOutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new SignUpException("Помилка при збереженні звіту користувачів: %s"
                .formatted(e.getMessage()));
        }
    }
}
