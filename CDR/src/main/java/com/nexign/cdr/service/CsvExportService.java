package com.nexign.cdr.service;

import com.nexign.cdr.entity.Call;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class CsvExportService {

    private static final String REPORT_DIR = "report";

    public void exportCalls(List<Call> calls) {
        File reportDir = new File(REPORT_DIR);
        if (!reportDir.exists() && !reportDir.mkdirs()) {
            log.error("Failed to create report directory");
            return;
        }

        File csvFile = new File(reportDir, "calls_" + System.currentTimeMillis() + ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (Call call : calls) {
                String line = String.format("%s,%s,%s,%s,%s",
                        call.getType(),
                        call.getClientMsisdn(),
                        call.getPartnerMsisdn(),
                        call.getStartTime(),
                        call.getEndTime());
                writer.write(line);
                writer.newLine();
            }
            log.info("Exported {} calls to {}", calls.size(), csvFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to export calls to CSV", e);
        }
    }

    public File getLatestCsvFile() {
        File dir = new File(REPORT_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        File[] csvFiles = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            return null;
        }

        return java.util.Arrays.stream(csvFiles)
                .max(java.util.Comparator.comparingLong(File::lastModified))
                .orElse(null);
    }

}
