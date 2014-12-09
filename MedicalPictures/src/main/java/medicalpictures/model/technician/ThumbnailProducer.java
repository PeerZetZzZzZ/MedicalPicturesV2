package medicalpictures.model.technician;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author PeerZet
 */
@Stateless
public class ThumbnailProducer {

    private static final Logger LOG = Logger.getLogger(ThumbnailProducer.class.getName());

    public byte[] getThumbnail(byte[] picture, String pictureName) throws IOException {
        String randomUuid = UUID.randomUUID().toString();
        String fileExtension = pictureName.substring(0, pictureName.lastIndexOf("."));
        File file = new File(randomUuid + fileExtension);
        while (file.exists()) {//if the thumbnail already exists
            randomUuid = UUID.randomUUID().toString();
            file = new File(randomUuid + pictureName);
        }
        file.createNewFile();
        LOG.info("Create the temporary file: " + fileExtension);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(picture);
        fos.flush();
        fos.close();//save file on disk
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(ImageIO.read(file).getScaledInstance(200, 200, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(img, fileExtension, new File("thumb" + file.getName()));
        String thumbnailName = "thumbnail" + pictureName + file.getName();
        ImageIO.write((RenderedImage) img, "jpg", new File(thumbnailName));
        File thumbnail = new File(thumbnailName);
        FileInputStream thumbnailStream = new FileInputStream(thumbnail);
        byte[] thumbnailBytes = IOUtils.toByteArray(thumbnailStream);
        file.delete();
        thumbnail.delete();
        LOG.info("Successfully retreived thumbnail from " + pictureName + ". Thumbnail size: ." + thumbnailBytes.length);
        LOG.info("Cleaning temporary created file");
        return thumbnailBytes;
    }
}
