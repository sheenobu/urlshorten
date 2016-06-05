package net.sheenobu.urlshorten.service;

import net.sheenobu.urlshorten.storage.URL;

public interface URLShortenService {
	public URL FindBySlug(String slug);

	public URL Create(java.net.URL url, int ttlInDays);

	public boolean Delete(URL url);
}
