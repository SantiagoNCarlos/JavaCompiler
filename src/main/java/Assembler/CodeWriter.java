package Assembler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CodeWriter {

    private static final String ASSEMBLER_CODE_FILE_NAME = "assembler.asm";
    private static final List<String> code_lines = new ArrayList<>();
    private PrintWriter writer_assembler_code;


    private static final class WriterHolder {
        private static final CodeWriter instance = new CodeWriter();
    }

    public static CodeWriter getInstance() {
        return CodeWriter.WriterHolder.instance;
    }

    private CodeWriter() {}

    public void writerInitializer(String fileSuffix) {
        try {
            writer_assembler_code = new PrintWriter(new FileWriter(fileSuffix + "-" + ASSEMBLER_CODE_FILE_NAME, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSentence(String line) {
        code_lines.add(line);
    }

    public void writeSentence(String line) {
        writer_assembler_code.println(line);     // Write to assembler file
        writer_assembler_code.flush();                 // Ensure data is written immediately
    }

    public void writeSentences() {
        for (String line : code_lines) {
            writeSentence(line);
        }
    }

    public void close() {
        if (writer_assembler_code != null) {
            writer_assembler_code.close();
        }
    }
}
