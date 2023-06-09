package ru.shafikova.UnpackingService.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.shafikova.UnpackingService.Models.FileMetadata;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnpackingService {
    public File unpack(MultipartFile file) throws IOException {

        List<String> codedLine = readBinFile(file);

        FileMetadata fileMetadata = new FileMetadata(codedLine.remove(0), codedLine.remove(0));
        System.out.println("fileMetadata.getOriginalFileName() " + fileMetadata.getOriginalFileName());
        System.out.println("fileMetadata.getOriginalFormat() " + fileMetadata.getOriginalFormat());

        System.out.println(codedLine);

        Decoder decoder = new Decoder();
        String decodedLine = decoder.unpack(codedLine);

        return writeOriginalFile(decodedLine, fileMetadata);
    }

    public List<String> readBinFile(MultipartFile file) throws IOException {
        File tmp = Files.createTempFile("data", null).toFile();
        file.transferTo(tmp);

        List<String> codedLine = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(tmp))) {
            short fileNameLength = dis.readShort();
            StringBuilder fileName = new StringBuilder();
            for (int i = 0; i < fileNameLength; i++) fileName.append(dis.readChar());
            codedLine.add(fileName.toString());

            short formatLength = dis.readShort();
            StringBuilder fileFormat = new StringBuilder();
            for (int i = 0; i < formatLength; i++) fileFormat.append(dis.readChar());
            codedLine.add(fileFormat.toString());

            while (dis.available() > 0) {
                int binaryNumber = dis.readShort();
                codedLine.add(String.valueOf(binaryNumber));

//                System.out.println(binaryNumber);
//                System.out.println("Прочитано двоичное число из файла: " + Integer.toBinaryString(binaryNumber));

            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        System.out.println(codedLine);

        tmp.deleteOnExit();

        return codedLine;
    }

    public File writeOriginalFile(String text, FileMetadata metadata) throws IOException {
        String fileName = (metadata.getOriginalFileName().split("\\.")[0] + "." + metadata.getOriginalFileName().split("\\.")[1]).replace(" ", "_");
        File decodedFile = new File(fileName);

        try (FileWriter fileWriter = new FileWriter(decodedFile)) {
            fileWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return decodedFile;
    }

}
