package xyz.maywr.jugg.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author maywr
 * 21.07.2022 0:06
 */
public interface Jsonable
{
	Gson GSON = new Gson();
	Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

}
