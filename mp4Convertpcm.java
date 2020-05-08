import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class mp4Convertpcm {
    public static void vedioToPcm(String vedioUrl, String pcmUrl, String ffmpegUrl) throws InterruptedException, IOException {
        List<String> commend = new ArrayList<String>();
        commend.add(ffmpegUrl);
        commend.add("-y");
        commend.add("-i");
        commend.add(vedioUrl);
        commend.add("-acodec");
        commend.add("pcm_s16le");
        commend.add("-f");
        commend.add("s16le");
        commend.add("-ac");
        commend.add("1");
        commend.add("-ar");
        commend.add("16000");
        commend.add(pcmUrl);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commend);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        process.waitFor();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        vedioToPcm("D:\\test\\vedio\\123456.mp4", "D:\\test\\vedio\\123456.pcm", "D:\\ffmpeg-static\\bin\\ffmpeg.exe");
        int start = 0;
        int end = 0;
        int count = 25;
        String sourcefile = "D:\\test\\vedio\\123456.pcm";
        long time = getTimeLen(new File(sourcefile));
        int newTime = (int) time;
        int internal = newTime - end;
        while (internal > 0) {
            if (internal < 10) {
                cut(sourcefile, "D:\\test\\vedio\\123456.pcm" + start + "_" + (int) time + ".pcm", start, (int) time);
                end += count;
                internal = newTime - end;
            } else {
                end += count;
                cut(sourcefile, "D:\\test\\vedio\\123456.pcm" + start + "_" + end + ".pcm", start, end);
                start += count;
                internal = newTime - end;
            }
        }

    }

    private static long getTimeLen(File file) {
        return 0;
    }

    public static boolean cut(String sourcefile, String targetfile, int start, int end) {
        try {
            if (!sourcefile.toLowerCase().endsWith(".pcm") || !targetfile.toLowerCase().endsWith(".pcm")) {
                return false;
            }
            File pcm = new File(sourcefile);
            if (!pcm.exists()) {
                return false;
            }
            long t1 = getTimeLen(pcm);  //总时长(秒)
            if (start < 0 || end <= 0 || start >= t1 || end > t1 || start >= end) {
                return false;
            }
            FileInputStream fis = new FileInputStream(pcm);
            long pcmSize = pcm.length() - 44;  //音频数据大小（44为128kbps比特率pcm文件头长度）
            long splitSize = (pcmSize / t1) * (end - start);  //截取的音频数据大小
            long skipSize = (pcmSize / t1) * start;  //截取时跳过的音频数据大小
            int splitSizeInt = Integer.parseInt(String.valueOf(splitSize));
            int skipSizeInt = Integer.parseInt(String.valueOf(skipSize));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return false;
    }
}
