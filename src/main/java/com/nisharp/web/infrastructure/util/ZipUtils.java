package com.nisharp.web.infrastructure.util;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * @author ZM.Wang
 */
public class ZipUtils {

    public static ByteArrayOutputStream createZip(List<ByteArrayOutputStream> outputStreams) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(byteArrayOutputStream)) {
                zaos.setUseZip64(Zip64Mode.AsNeeded);
                int i = 1;
                for (ByteArrayOutputStream outputStream : outputStreams) {
                    ZipEntry entry = new ZipEntry("image" + i + ".jpg");
                    entry.setMethod(8);
                    ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(entry);
                    zaos.putArchiveEntry(zipArchiveEntry);
                    zaos.write(outputStream.toByteArray());
                    zaos.closeArchiveEntry();
                    i = ++i;
                }
                zaos.finish();
                return byteArrayOutputStream;
            }
        }
    }

}
