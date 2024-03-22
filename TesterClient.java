import java.rmi.registry.*;

public class TesterClient {
    RemoteHistogram histogramRMI = null;
    String host = "localhost";
    public TesterClient() {
        try {
            Registry r = LocateRegistry.getRegistry(host, 1099);
            Object o = r.lookup("HistogramTester");
            histogramRMI = (RemoteHistogram) o;
        } catch (Exception e) {
            System.err.println("Cannot acces the RMI registry!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean testHistogram(int bins, boolean random_values) {
        try {
            // Create histogram
            int myid = histogramRMI.createHistogram(bins);

            // Calculate correct results
            int[] correctResult = new int[bins];

            // Fill array with 0
            for (int i = 0; i < bins; i++) {
                correctResult[i] = 0;
            }

            // Add values to histogram
            for (int i = 0; i < bins; i++) {
                if (random_values) {
                    int random_value = getRandomNumber(0, bins);
                    correctResult[random_value]++;
                    histogramRMI.addToHistogram(myid, random_value);
                } else {
                    correctResult[i]++;
                    histogramRMI.addToHistogram(myid, i);
                }
            }

            // Get histogram data
            int[] result = histogramRMI.getHistogram(myid);

            // Check if histogram data is correct
            boolean correct = true;
            for (int i = 0; i < bins; i++) {
                if (result[i] != correctResult[i]) {
                    correct = false;
                    break;
                }
            }
            return correct;
        } catch (Exception e) {
            System.err.println("Cannot access the RMI registry!");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}