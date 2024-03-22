import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class Tester {

    // How many histograms do you want to generate PER CLIENT
    public static final int HISTOGRAMS_PER_CLIENT = 100000;

    // How many threads do you want to run
    public static final int THREADS = 8;

    // Maximum amount of bins that can be in one histogram
    public static final int MAX_BINS = 100;

    public static void main(String[] args) {
        try {
            RMIHistogram server = new RMIHistogram();
            LocateRegistry.createRegistry(1099);
            server.bind("HistogramTester");

            // Create clients
            ArrayList<ClientThread> clients = new ArrayList<>();
            for (int i = 0; i < THREADS; i++) {
                clients.add(new ClientThread(i, getRandomNumber(0, MAX_BINS), true));
            }

            // Start clients
            for (ClientThread client : clients) {
                client.start();
            }

            // Wait for clients to finish
            for (ClientThread client : clients) {
                client.join();
            }

            // Print results
            int total_successes = 0;
            for (ClientThread client : clients) {
                total_successes += client.getSuccesses();
            }
            System.out.println("\n\nResults:");
            System.out.println("Total successes: " + total_successes);
            System.out.println("Total failures: " + (HISTOGRAMS_PER_CLIENT * THREADS - total_successes));
            System.out.println("Success rate: " + (total_successes / (HISTOGRAMS_PER_CLIENT * THREADS)) * 100 + "%");
        }
        catch (Exception e) {
            System.out.println("Exception while starting server: " + e.getMessage());
        }
    }

    static class ClientThread extends Thread {
        private int id;
        private int bins;
        private boolean random_values;
        private TesterClient client;

        int successes = 0;

        public ClientThread(int id, int bins, boolean random_values) {
            this.id = id;
            this.bins = bins;
            this.random_values = random_values;
            this.client = new TesterClient();
        }

        public int getSuccesses() {
            return successes;
        }

        public void run() {
            // Small random sleep to simulate different start times
            try {
                Thread.sleep(getRandomNumber(0, 1000));
            }
            catch (Exception e) {
                System.out.println("Exception while sleeping: " + e.getMessage());
            }

            boolean success;
            for (int i = 0; i < HISTOGRAMS_PER_CLIENT; i++) {
                success = client.testHistogram(bins, random_values);
                if(success) {
                    successes++;
                    System.out.println("[Client " + id + " | Hist. " + i + "] Test passed!");
                }
                else {
                    System.out.println("[Client " + id + " | Hist. " + i + "] Test failed!");
                }
            }
        }
    }

    private static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
