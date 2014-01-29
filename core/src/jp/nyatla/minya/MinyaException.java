package jp.nyatla.minya;

public class MinyaException extends Exception
{
	private static final long serialVersionUID = 3363L;
	public MinyaException()
	{
		super();
	}
	public MinyaException(Throwable e)
	{
		super(e);
	}
	public MinyaException(String s)
	{
		super(s);
	}
}