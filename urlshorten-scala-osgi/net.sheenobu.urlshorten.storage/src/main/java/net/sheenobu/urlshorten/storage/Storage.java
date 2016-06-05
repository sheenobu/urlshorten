package net.sheenobu.urlshorten.storage;

public interface Storage {

	public URL findBySlug(String slug);

	public URL create(java.net.URL url, int ttlInDays);

	public boolean delete(String slug);
}
