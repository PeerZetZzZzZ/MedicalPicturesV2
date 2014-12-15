package medicalpictures.model.technician;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import medicalpictures.model.common.MedicalLogger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author PeerZet
 */
@Stateless
public class ThumbnailProducer {

    @EJB
    private MedicalLogger logger;

    /**
     * Gets the thumbnail of the big picture data.
     *
     * @param picture
     * @param pictureName
     * @return
     * @throws IOException
     */
    public byte[] getThumbnail(byte[] picture, String pictureName) throws IOException {
        String randomUuid = UUID.randomUUID().toString();
        String fileExtension = pictureName.substring(0, pictureName.lastIndexOf("."));
        File file = new File(randomUuid + fileExtension);
        while (file.exists()) {//if the thumbnail already exists
            randomUuid = UUID.randomUUID().toString();
            file = new File(randomUuid + pictureName);
        }
        file.createNewFile();
        logger.logInfo("Create the temporary file: " + fileExtension, ThumbnailProducer.class);
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
        logger.logInfo("Successfully retreived thumbnail from " + pictureName + ". Thumbnail size: ." + thumbnailBytes.length, ThumbnailProducer.class);
        logger.logInfo("Cleaning temporary created file", ThumbnailProducer.class);
        return thumbnailBytes;
    }
}
