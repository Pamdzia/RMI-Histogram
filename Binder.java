/**
 * Interfejs rejestracji usĹugi RMI.
 */
public interface Binder {
	/**
	 * Rejestruje w rmiregistry pod podanÄ nazwÄ serwer usĹugi. UsĹuga rmiregistry
	 * bÄdzie uruchomiona pod adresem localhost:1099. <b>Metoda nie moĹźe
	 * samodzielnie uruchamiaÄ rmiregistry!</b>
	 * 
	 * @param serviceName oczekiwana nazwa usĹugi w rmiregistry
	 */
	public void bind(String serviceName);
}