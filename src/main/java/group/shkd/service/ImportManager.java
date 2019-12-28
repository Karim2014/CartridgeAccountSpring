package group.shkd.service;

import group.shkd.model.Cartridge;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ImportManager {

    private static final String FILE_NAME = "import.xlsx";

    private static final String SQL =
            "INSERT INTO cartridge (producer, name, num, state, note) " +
                    "VALUES ((SELECT id FROM producer WHERE title LIKE ?), ?, ?, (SELECT id FROM states WHERE title LIKE ?), ?)";

    private JdbcTemplate template;

    @Autowired
    public ImportManager(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public void execute() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(FILE_NAME)));
        Sheet sheet = workbook.getSheet("Основной список");
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next();
        Row row;
        Cell cell;
        String cartridge = "";
        String num = "";
        String note = "";
        String state = "";
        List<Cartridge> cartridges = new ArrayList<>();
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            cell = row.getCell(0);
            if (cell != null) {
                cartridge = cell.getStringCellValue();
            }
            if (cartridge.equals("")) {
                continue;
            }
            cell = row.getCell(1);
            if (cell != null) {
                num = cell.getStringCellValue();
            }
            cell = row.getCell(2);
            if (cell != null) {
                note = cell.getStringCellValue();
            }
            cell = row.getCell(3);
            if (cell != null) {
                state = cell.getStringCellValue();
            }
            String[] producer_name = cartridge.split(" ", 2);

            template.update(SQL, producer_name[0], producer_name[1], num, state, note);
        }
    }

}
