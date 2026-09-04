package api.endpoints;

public class Routes {

	public static String base_url = "https://petstore.swagger.io/v2";

	// User module
	public static String post_url = base_url + "/user";
	public static String get_url = base_url + "/user/{username}";
	public static String update_url = base_url + "/user/{username}";
	public static String delete_url = base_url + "/user/{username}";
	public static String login_url = base_url + "/user/login";
	public static String logout_url = base_url + "/user/logout";
	public static String post_createWithListArray_url = base_url + "/user/createWithList";
	public static String post_createWithArray_url = base_url + "/user/createWithArray";

	// Store module
	public static String get_inventoriesByStatus_url = base_url + "/store/inventory";
	public static String post_placeAnOrderForAPet_url = base_url + "/store/order";
	public static String get_findPurchaseOrderById_url = base_url + "/store/order/{orderId}";
	public static String delete_PurchaseOrderById_url = base_url + "/store/order/{orderId}";

	// Pet module
	public static String post_uploadImage_url = base_url + "/pet/{petId}/uploadImage";
	public static String post_addNewPet_url = base_url + "/pet";
	public static String put_updatePet_url = base_url + "/pet";
	public static String get_findPetByStatus_url = base_url + "/pet/findByStatus";
	public static String get_findPetById_url = base_url + "/pet/{petId}";
	public static String post_updatePetForm_url = base_url + "/pet/{petId}";
	public static String delete_deletePet_url = base_url + "/pet/{petId}";

	

}
