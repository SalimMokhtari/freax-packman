package it.freax.fpm.core.util;

import it.freax.fpm.core.exceptions.ExtensionDecodingException;

import java.util.*;
import java.util.regex.Pattern;

public class Strings extends Constants
{
	public Strings()
	{
	}

	public static Strings getOne()
	{
		return new Strings();
	}

	public boolean checkExtensions(String path, List<String> list) throws ExtensionDecodingException
	{
		boolean ret = false;
		if (!getExtension(path).isEmpty())
		{
			for (String ext : list)
			{
				if (ret = checkExtension(path, ext))
				{
					break;
				}
			}
		}
		return ret;
	}

	public boolean checkExtension(String path, String ext) throws ExtensionDecodingException
	{
		return ext.equalsIgnoreCase(getExtension(path));
	}

	public String getExtension(String path) throws ExtensionDecodingException
	{
		String ext = "";
		ArrayList<String> candidates = new ArrayList<String>();
		String[] splitted = path.split("\\" + extSeparator);
		candidates.add(splitted[splitted.length - 1]);
		for (int i = splitted.length - 2; i > 0; i--)
		{
			if (!splitted[i].isEmpty() && inRangeExt(splitted[i]))
			{
				candidates.add(splitted[i]);
			}
		}
		if (!path.endsWith(ext)) { throw new ExtensionDecodingException("Extension decoding failed!"); }
		if (candidates.size() > 0)
		{
			ext = extSeparator + Collections.getOne(candidates).lastOrDefault();
		}
		return ext;
	}

	private boolean inRangeExt(String ext)
	{
		return (ext.length() >= rangeExtMin) && (ext.length() <= rangeExtMax);
	}

	public String removeExtension(String fileName) throws ExtensionDecodingException
	{
		return fileName.replace(getExtension(fileName), "");
	}

	public String replaceExtension(String fileName, String extToReplace) throws ExtensionDecodingException
	{
		return fileName.replace(getExtension(fileName), extToReplace);
	}

	public String trimEnd(String str)
	{
		str = str.trim();
		int idx = str.lastIndexOf(' ');
		if ((str != null) && !str.equalsIgnoreCase(""))
		{
			str = str.substring(0, idx);
		}
		return str;
	}

	public String trimStart(String str)
	{
		str = str.trim();
		int idx = str.lastIndexOf(' ') + 1;
		if ((str != null) && !str.equalsIgnoreCase(""))
		{
			str = str.substring(idx, str.length());
		}
		return str;
	}

	public String getStringInsideDelimiters(String input, String startDelimiter, String endDelimiter)
	{
		String ret = "";
		if ((input != null) && !input.isEmpty())
		{
			ret = input.substring(input.indexOf(startDelimiter) + 1);
			if (!ret.equalsIgnoreCase(input))
			{
				ret = ret.substring(0, ret.indexOf(endDelimiter));
			}
		}
		return ret;
	}

	public String getStringFromKeyValue(String input, String keyValueDelimiter, boolean getKey)
	{
		String ret = "";
		StringTokenizer st = new StringTokenizer(input, keyValueDelimiter);
		if (getKey)
		{
			ret = st.nextToken();
		}
		else
		{
			st.nextToken();
			if (st.hasMoreTokens())
			{
				ret = st.nextToken();
			}
		}
		return ret;
	}

	public String clean(String input)
	{
		return input.replace("[", "").replace("]", "");
	}

	public String getRowSubstring(String input, String subset, boolean removeSubSet)
	{
		int beginIndex = input.indexOf(subset);
		if (removeSubSet)
		{
			beginIndex += subset.length();
		}
		return input.substring(beginIndex);
	}

	public List<String> getLines(String input)
	{
		List<String> ret = new ArrayList<String>();
		Scanner scn = new Scanner(input);
		while (scn.hasNext())
		{
			ret.add(scn.next());
		}
		return ret;
	}

	public String getLines(Collection<String> input)
	{
		StringBuffer ret = new StringBuffer();
		String[] inputArr = input.toArray(new String[input.size()]);
		for (int i = 0; i < inputArr.length; i++)
		{
			ret.append(inputArr[i]).append(System.getProperty("line.separator"));
		}
		return ret.toString();
	}

	public List<String> grep(String input, String pattern, boolean caseInsensitive)
	{
		ArrayList<String> ret = new ArrayList<String>();
		Scanner scn = new Scanner(input);
		int Options = 0;
		if (caseInsensitive)
		{
			Options |= Pattern.CASE_INSENSITIVE;
		}
		Pattern patRegex = Pattern.compile(pattern, Options);
		while (scn.hasNext())
		{
			String curr = scn.nextLine();
			if (patRegex.matcher(curr).matches())
			{
				ret.add(curr);
			}
		}
		return ret;
	}

	public String Split(String input, String delim, int elemidx, boolean remext)
	{
		String ret = "";
		if (remext)
		{
			try
			{
				ret = removeExtension(input);
			}
			catch (ExtensionDecodingException e)
			{
				e.printStackTrace();
			}
		}
		ret = input.split(delim)[elemidx];
		return ret;
	}

	private int getOpID(String input)
	{
		int opid = 0;
		char first = input.charAt(0);
		switch (first)
		{
			case '"':
			{
				opid = 1;
				break;
			}
			case '\'':
			{
				opid = 2;
				break;
			}
			case '<':
			{
				opid = 3;
				break;
			}
			case '{':
			{
				opid = 4;
				break;
			}
			case '[':
			{
				opid = 5;
				break;
			}
			case '(':
			{
				opid = 6;
				break;
			}
		}
		return opid;
	}

	public String KeyValue(String input, String delim)
	{
		String ret = "";
		ret = getStringFromKeyValue(input, delim, false);
		int opid = getOpID(ret);
		switch (opid)
		//gli esempi sono tutti con uguale anche se il delimiter è dinamico
		{
			case 0: // Semplice, Chiave=Valore
			{
				break;
			}
			case 1:// Con virgolette, Chiave="Valore"
			{
				ret = getStringInsideDelimiters(ret, "\"", "\"");
				break;
			}
			case 2: // Con apici, Chiave='Valore'
			{
				ret = getStringInsideDelimiters(ret, "'", "'");
				break;
			}
			case 3: // Con parentesi angolari, Chiave=<Valore>
			{
				ret = getStringInsideDelimiters(ret, "<", ">");
				break;
			}
			case 4: // Con parentesi graffe, Chiave={Valore}
			{
				ret = getStringInsideDelimiters(ret, "{", "}");
				break;
			}
			case 5: // Con parentesi quadre, Chiave=[Valore]
			{
				ret = getStringInsideDelimiters(ret, "[", "]");
				break;
			}
			case 6: // Con parentesi tonde, Chiave=(Valore)
			{
				ret = getStringInsideDelimiters(ret, "(", ")");
				break;
			}
		}
		return ret;
	}

	public String trimDir(String file)
	{
		int idx = file.lastIndexOf(System.getProperty("file.separator")) + 1;
		file = file.substring(idx);
		return file;
	}

	public List<String> split(String str, String delim)
	{
		List<String> ret = new ArrayList<String>();
		if (str != null)
		{
			ret = Collections.<String> getOne(str.split(delim)).toList();
		}
		return ret;
	}

	public String merge(List<String> toMerge, String delimiter)
	{
		StringBuffer merged = new StringBuffer();
		for (int i = 0; i < toMerge.size(); i++)
		{
			merged.append(toMerge.get(i));
			if (i < toMerge.size() - 1)
			{
				merged.append(delimiter);
			}
		}
		return merged.toString();
	}

	public String concatPaths(String... args)
	{
		String ret = args[0];
		final String FS = System.getProperty("file.separator");
		for (int i = 1; i < args.length; i++)
		{
			if (!ret.endsWith(FS))
			{
				ret += FS;
			}
			if (args[i].endsWith(FS))
			{
				ret += args[i];
			}
			else
			{
				ret += args[i] + FS;
			}
		}
		return ret;
	}
}