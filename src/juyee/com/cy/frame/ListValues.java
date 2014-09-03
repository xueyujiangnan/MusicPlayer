package juyee.com.cy.frame;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/9/2.
 */
public class ListValues implements Serializable {
    private String fileName;
    private String dirFileName;

    ListValues(){
        setFileName("歌曲名字");
        setDirFileName("E:\\歌曲名字");
    }
    ListValues(String fileNameC,String dirFileNameC)
    {
        setFileName(fileNameC);
        setDirFileName(dirFileNameC);
    }

    void setFileName(String fileNameC)
    {
        fileName=fileNameC;
    }
    void setDirFileName(String dirFileNameC)
    {
        dirFileName=dirFileNameC;
    }
    String getFileName()
    {
        return fileName;
    }
    String getDirName()
    {
        return dirFileName;
    }
}