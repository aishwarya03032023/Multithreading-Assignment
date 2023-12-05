/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package multithreadapplicationdemo;

/**
 *
 * @author Ash
 */

public class MultiThreadApplicationDemo {

    public static void main(String[] args) {
        playNotes();
    }

    private static void playNotes() {
        Object lock = new Object();
        Thread thread1 = new Thread(new MusicThread(lock, "Thread 1",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\do.wav",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\mi.wav",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\sol.wav",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\si.wav",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\do-octave.wav"));
        Thread thread2 = new Thread(new MusicThread(lock, "Thread 2",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\re.wav",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\fa.wav",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\la.wav",
                "D:\\Users\\Admin\\Documents\\NetBeansProjects\\MusicPlayer\\do-octave.wav"));

        thread1.start();
        thread2.start();
    }
}

class MusicThread implements Runnable {

    private final Object lock;
    private final String threadName;
    private final String[] tones;

    public MusicThread(Object lock, String threadName, String... tones) {
        this.lock = lock;
        this.threadName = threadName;
        this.tones = tones;
    }

    @Override
    public void run() {
        for (String tone : tones) {
            synchronized (lock) {
                // Play the tone
                playTone(tone);
                // Notify the other thread to proceed
                lock.notify();
                try {
                    // Wait for the other thread to finish playing its tone
                    if (!tone.equals(tones[tones.length - 1])) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void playTone(String tone) {
        FilePlayer player = new FilePlayer();
        System.out.println("Thread: " + threadName + " - Playing Note: " + tone);
        player.play(tone);
    }
}