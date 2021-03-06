/**
 * 
 */
package it.freax.fpm.test.main;

import java.io.File;
import java.io.FileNotFoundException;

import it.freax.fpm.core.specs.tarball.antlr.Language;
import it.freax.fpm.util.exceptions.ExtensionDecodingException;

/**
 * @author klez
 */
public class TestLanguageGrammars
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String path = "/home/klez/projects/FreaxPackageManager/fpm-core/src/grammars/Java.g";
		try
		{
			System.out.println(Language.create(new File(path), "Java"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (ExtensionDecodingException e)
		{
			e.printStackTrace();
		}
	}
}
