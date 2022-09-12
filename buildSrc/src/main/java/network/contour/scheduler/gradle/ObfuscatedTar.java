package network.contour.scheduler.gradle;

import org.gradle.api.file.RegularFile;
import org.gradle.api.internal.file.archive.TarCopyAction;
import org.gradle.api.internal.file.archive.compression.ArchiveOutputStreamFactory;
import org.gradle.api.internal.file.archive.compression.Bzip2Archiver;
import org.gradle.api.internal.file.archive.compression.GzipArchiver;
import org.gradle.api.internal.file.archive.compression.SimpleCompressor;
import org.gradle.api.internal.file.copy.CopyAction;
import org.gradle.api.tasks.bundling.Tar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class ObfuscatedTar extends Tar {

    public ObfuscatedTar() {
        getArchiveExtension().set("bin");
    }

    private ArchiveOutputStreamFactory getCompressor() {
        ArchiveOutputStreamFactory archiveOutputStreamFactory;
        switch (this.getCompression()) {
            case BZIP2:
                archiveOutputStreamFactory = Bzip2Archiver.getCompressor();
            case GZIP:
                archiveOutputStreamFactory = GzipArchiver.getCompressor();
            default:
                archiveOutputStreamFactory = new SimpleCompressor();
        }
        ArchiveOutputStreamFactory archiveOutputStreamFactoryDelegate = archiveOutputStreamFactory;
        return new ArchiveOutputStreamFactory() {
            @Override
            public OutputStream createArchiveOutputStream(File file) throws IOException {
                return new XorOutputStream(archiveOutputStreamFactoryDelegate.createArchiveOutputStream(file));
            }
        };
    }

    protected CopyAction createCopyAction() {
        return new TarCopyAction(((RegularFile)this.getArchiveFile().get()).getAsFile(), this.getCompressor(), this.isPreserveFileTimestamps());
    }
}
