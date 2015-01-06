package medicalpictures.model.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Medical logger used to log every important event in application. Log is made
 * in standard output ( for example Glassfish output ) and log file (log file
 * location for glassfish:
 * glassfishInstallation/glassfish/domains/domain1/config/ ). Log file name is
 * today's date in such form: YYYY-MM-DD.txt
 *
 * @author Przemysław Thomann
 */
@Singleton
public class MedicalLogger {

    private static final Logger LOG = Logger.getLogger(MedicalLogger.class.getName());
    private File logFile;
    private boolean logFileCreated = false;
    private PrintWriter logOutput;

    @PostConstruct
    public void initLogFile() {
        LocalDate now = new LocalDate();
        String logFileName = "../" + now.toString() + ".txt";
        logFile = new File(logFileName);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
                LOG.log(Level.INFO, "Created log file: {0}. Log file path: {1}", new Object[]{logFileName, logFile.getAbsolutePath()});
                logOutput = new PrintWriter(new FileWriter(logFile, true));
                LOG.log(Level.INFO, "Created writer for log file: {0}", logFileName);
                logFileCreated = true;//successfully craeted log file
            } else {
                logOutput = new PrintWriter(new FileWriter(logFile, true));
                logFileCreated = true;
            }
        } catch (IOException ex) {
            LOG.log(Level.INFO, "ERROR! Couldn''t create log file: {0}", logFileName);
            logFileCreated = false;//creation failed
            Logger.getLogger(MedicalLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void logInfo(String message, Class infoClass) {
        if (logFileCreated) {
            DateTime now = new DateTime();
            String infoMessage = "[" + now.toString() + "]" + "[" + infoClass.getName() + "]" + "[INFO] " + message;
            logOutput.println(infoMessage);
            logOutput.flush();
            LOG.info(infoMessage);
        }
    }

    public void logError(String message, Class infoClass, Throwable ex) {
        if (logFileCreated) {
            DateTime now = new DateTime();
            String errorMessage = "[" + now.toString() + "]" + "[" + infoClass.getName() + "]" + "[ERROR] " + message;
            logOutput.println(errorMessage);
            logOutput.println("[" + now.toString() + "]" + "[" + infoClass.getName() + "]" + "[STACKTRACE] ");
            logOutput.println("");
            logOutput.flush();
            for (StackTraceElement element : ex.getStackTrace()) {
                logOutput.println("   " + element.toString());
            }
            logOutput.flush();
            logOutput.println("");
            logOutput.flush();
            LOG.info(errorMessage);
            Logger.getLogger(infoClass.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void logWarning(String message, Class infoClass) {
        if (logFileCreated) {
            DateTime now = new DateTime();
            String warningMessage = "[" + now.toString() + "]" + "[" + infoClass.getName() + "]" + "[WARNING] " + message;
            logOutput.println(warningMessage);
            logOutput.println("[" + now.toString() + "]" + "[" + infoClass.getName() + "]" + "[STACKTRACE] ");
            logOutput.println("");
            logOutput.flush();
            LOG.info(warningMessage);
        }
    }

    @PreDestroy
    public void closeAll() {
        if (logFileCreated) {
            logOutput.flush();
            logOutput.close();
        }
    }
}
