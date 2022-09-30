package ch.bfh.memory.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


import ch.bfh.memory.models.MemoryPair;

public class DataUtil {

    public static ArrayList<MemoryPair> MemoryPairs = new ArrayList<>();

    /**
     * This method loads all MemoryPairs
     *
     * @param fis
     */
    public static void loadPairsFile(FileInputStream fis) {

        try {
            ObjectInputStream is = new ObjectInputStream(fis);


            MemoryPair mCard = (MemoryPair) is.readObject();

            while (mCard != null) {

                MemoryPairs.add(mCard);

                mCard = (MemoryPair) is.readObject();
            }

            is.close();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * This method writes all MemoryPairs into a textfile
     *
     * @param fos
     */
    public static void writePairsFile(FileOutputStream fos) {


        try {

            ObjectOutputStream os = new ObjectOutputStream(fos);


            for (MemoryPair card :
                    MemoryPairs) {
                os.writeObject(card);
            }

            os.close();
            fos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
