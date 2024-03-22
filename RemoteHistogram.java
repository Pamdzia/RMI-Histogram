import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfejs zdalnego serwisu tworzÄcego histogramy.
 */
public interface RemoteHistogram extends Remote {
	/**
	 * Utworzenie histogramu o okreĹlonej liczbie kubeĹkĂłw.
	 * 
	 * @param bins liczba kubeĹkĂłw
	 * @return unikalny numer identyfikujÄcy histogram
	 * @throws RemoteException
	 */
	public int createHistogram(int bins) throws RemoteException;

	/**
	 * Dodanie danej do histogramu o wskazanym identyfikatorze.
	 * 
	 * @param histogramID identyfikator histogramu, do ktĂłrego naleĹźy dodaÄ value
	 * @param value       wartoĹÄ do dodania do histogramu histogramID
	 * @throws RemoteException
	 */
	public void addToHistogram(int histogramID, int value) throws RemoteException;

	/**
	 * Pobranie histogramu o wskazanym identyfikatorze.
	 * 
	 * @param histogramID identyfikator histogramu
	 * @return tablica o rozmiarze bins z histogramem
	 * @throws RemoteException
	 */
	public int[] getHistogram(int histogramID) throws RemoteException;

}