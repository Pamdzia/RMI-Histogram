import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RMIHistogram extends UnicastRemoteObject implements RemoteHistogram, Binder {

    private final ConcurrentHashMap<Integer, int[]> histograms = new ConcurrentHashMap<>();
    private int nextId = 1;

    protected RMIHistogram() throws RemoteException {
        super();
    }

   @Override
    public synchronized int createHistogram(int bins) throws RemoteException {
        int histogramId = nextId++;
        histograms.put(histogramId, new int[bins]);
        return histogramId;
    }

    @Override
    public void addToHistogram(int histogramID, int value) throws RemoteException {
        int[] histogram;
        synchronized (this) {
            histogram = histograms.get(histogramID);
            if (histogram == null) {
                throw new IllegalArgumentException("Invalid histogram ID");
            }
        }
        synchronized (histogram) {
            if (value >= 0 && value < histogram.length) {
                histogram[value]++;
            } else {
                throw new IllegalArgumentException("Value out of range");
            }
        }
    }

    @Override
    public int[] getHistogram(int histogramID) throws RemoteException {
        int[] histogram = histograms.get(histogramID);
        if (histogram == null) {
            throw new IllegalArgumentException("Invalid histogram ID");
        }
        return histogram;
    }

    public void bind(String serviceName) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(serviceName, this);
            System.out.println("Service bound as " + serviceName);
        } catch (Exception e) {
            System.out.println("Exception binding service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}