package medicalpictures.model.common;

/**
 * Contains informations about result codes returned to the client.
 *
 * @author Przemysław Thomann
 */
public class ResultCodes {

	public static ResultCodes INSTANCE = new ResultCodes();

	private ResultCodes() {

	}

	/**
	 * When operation finished successfully.
	 */
	public static int OPERATION_SUCCEED = 0;
	/**
	 * When internal server problem occurred
	 */
	public static int INTERNAL_SERVER_ERROR = -99;
	/**
	 * When user is not authorized to access specified content.
	 */
	public static int USER_UNAOTHRIZED = -1;
	/**
	 * When it was error while parsing input JSON.
	 */
	public static int INPUT_JSON_PARSE_ERROR = -3;
	/**
	 * When it was error while parsing input JSON.
	 */
	public static int USER_IS_NOT_LOGGED = -4;
	/**
	 * When user authentication failed while login.
	 */
	public static int USER_AUTHENTICATION_FAILED = -1000;

	/**
	 * When object doesn't exist for example GROUP ( except USER, it has own code )
	 */
	public static int OBJECT_DOESNT_EXIST = -6;
	/**
	 * When user doesn't exist.
	 */
	public static int USER_DOESNT_EXIST = -5;

}
