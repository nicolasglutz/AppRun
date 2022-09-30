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

import ch.bfh.memory.models.MemoryCard;
import ch.bfh.memory.models.MemoryPair;

public class DataUtil {

    public static ArrayList<MemoryCard> MemoryCards = new ArrayList<>();
    public static ArrayList<MemoryPair> MemoryPairs = new ArrayList<>();


    /**
     * This method must be used to create a new memory pair, because if you would just add
     * a new pair to the MemoryPairs array then there would be duplicates.
     *
     * @param memoryPair
     */
    public static void addMemoryPair(MemoryPair memoryPair) {

        //add to collection
        MemoryPairs.add(memoryPair);

        //remove from memorycards collection because of duplication
        MemoryCards.remove(memoryPair.cardTwo);
        MemoryCards.remove(memoryPair.cardOne);

    }

    /**
     * This method loads all Memorycards
     * @param fis
     */
//

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


//    public static void loadCardsFile(FileInputStream fis) {
//
//        try{
//
////            FileInputStream fis = getApplicationContext().openFileInput("cards.txt");
//            ObjectInputStream is = new ObjectInputStream(fis);
//
//            MemoryCard mCard = (MemoryCard) is.readObject();
//
//            while (mCard != null){
//
//                MemoryCards.add(mCard);
//
//                mCard = (MemoryCard) is.readObject();
//            }
//
//            Log.d("SERIALIZECARDS", "loadObjectFile: " + MemoryCards.size());
//
//            is.close();
//            fis.close();
//
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    /**
//     * This method writes all MemoryCards into a textfile
//     * @param fos
//     */
//    public static void writeCardsFile(FileOutputStream fos){
//
//
//        try {
//
////            FileOutputStream fos = null;
////            fos = getApplicationContext().openFileOutput("cards.txt", Context.MODE_PRIVATE);
//
//            ObjectOutputStream os = new ObjectOutputStream(fos);
//
//            for (MemoryCard card:
//                 MemoryCards) {
//                os.writeObject(card);
//            }
//
////            os.writeObject(new MemoryCard("dasdis","dsfsdfds"));
//            os.close();
//            fos.close();
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


//    }
}
