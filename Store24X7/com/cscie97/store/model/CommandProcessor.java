package com.cscie97.store.model;

import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.CredentialType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * CommandProcessor processes input commands.
 * Commands are strings read from the command line or from a file.
 */
public class CommandProcessor {
	private final StoreModelService storeModelService = StoreModelService.getInstance();
	private HashMap<String, String> wordMap = new HashMap<>();
	private final AuthenticationService authService = AuthenticationService.getInstance();

	public CommandProcessor() {
	}

	/**
	 * Processes commands and calls appropriate ledger methods to carry out command function.
	 * Displays successful command returns (if any) to std out.
	 * Catches LedgerExceptions and throws CommandProcessorExceptions on invalid inputs.
	 *
	 * @param command the single-line command to be processed
	 * @return
	 */
	public String processCommand(String command, String authToken) {
		System.out.println("processing command: " + command);
		String[] commandList = command.split("\\s+");
		try {
			switch (commandList[0].strip()) {
				case "define-store":
					String[] storeKeyWords = {"define-store", "name", "address"};
					// Parse the string
					commandParser(command, storeKeyWords);
					String id = wordMap.get("define-store");
					String name = wordMap.get("name");
					String address = wordMap.get("address");
					storeModelService.defineStore(id, name, address, authToken);
					break;

				case "show-store":
					String storeId = commandList[1].strip();
					storeModelService.showStore(storeId, authToken);
					break;

				case "define-aisle":
					String[] aisleKeyWords = {"define-aisle", "name", "description", "location"};
					// Parse the string
					commandParser(command, aisleKeyWords);
					storeModelService.defineAisle(wordMap.get("define-aisle"),
							wordMap.get("name"),
							wordMap.get("description"),
							wordMap.get("location"), authToken);

					break;

				case "show-aisle":
					storeModelService.showAisle(commandList[1].strip(), authToken);
					break;

				case "define-shelf":
					String[] shelfKeywords = {"define-shelf", "name", "level", "description", "temperature"};
					commandParser(command, shelfKeywords);
					storeModelService.defineShelf(wordMap.get("define-shelf"),
							wordMap.get("name"),
							wordMap.get("level"),
							wordMap.get("description"),
							wordMap.get("temperature"), authToken);

					break;

				case "show-shelf":
					storeModelService.showShelf(commandList[1].strip(), authToken);
					break;

				case "define-inventory":
					String[] inventoryKeywords = {"define-inventory", "location", "capacity", "count", "product"};
					commandParser(command, inventoryKeywords);
					int capacity = Integer.parseInt(wordMap.get("capacity")),
							count = Integer.parseInt(wordMap.get("count"));

					storeModelService.defineInventory(wordMap.get("define-inventory"), wordMap.get("location"),
							capacity, count, wordMap.get("product"), authToken);

					break;

				case "show-inventory":
					storeModelService.showInventory(commandList[1].strip(), authToken);
					break;

				case "update-inventory":
					String[] updateInvKeywords = {"update-inventory", "update_count"};
					commandParser(command, updateInvKeywords);
					int update_count = Integer.parseInt(wordMap.get("update_count"));

					storeModelService.updateInventory(wordMap.get("update-inventory"), update_count, authToken);
					break;

				case "define-product":
					String[] productKeywords = {"define-product", "name", "description",
							"size", "category", "unit_price", "temperature"};
					commandParser(command, productKeywords);
					int unit_price = Integer.parseInt(wordMap.get("unit_price"));

					storeModelService.defineProduct(wordMap.get("define-product"), wordMap.get("name"), wordMap.get("description"),
							wordMap.get("size"), wordMap.get("category"), unit_price, wordMap.get("temperature"), authToken);

					break;

				case "show-product":
					storeModelService.showProduct(commandList[1].strip(), authToken);
					break;

				case "define-customer":
					String[] customerKeywords = {"define-customer", "first_name", "last_name",
							"isRegistered", "isAdult", "email_address", "account"};
					commandParser(command, customerKeywords);
					Boolean isRegistered = Boolean.parseBoolean(wordMap.get("isRegistered"));
					Boolean isAdult = Boolean.parseBoolean(wordMap.get("isAdult"));
					storeModelService.defineCustomer(wordMap.get("define-customer"), wordMap.get("first_name"), wordMap.get("last_name"),
							isRegistered, isAdult, wordMap.get("email_address"), wordMap.get("account"), authToken);
					break;

				case "update-customer":
					String[] updateCustKeywords = {"update-customer", "location"};
					commandParser(command, updateCustKeywords);
					String custId = wordMap.get("update-customer");
					String location = wordMap.get("location");
					storeModelService.updateCustomer(custId, location, authToken);
					break;

				case "show-customer":
					storeModelService.showCustomer(commandList[1].strip(), authToken);
					break;

				case "get-customer-location":
					return storeModelService.getCustomerLocation(commandList[1].strip(), authToken);

				case "define-basket":
					String[] basketKeywords = {"define-basket", "location"};
					commandParser(command, basketKeywords);
					storeModelService.defineBasket(wordMap.get("define-basket"), wordMap.get("location"), authToken);
					break;

				case "assign-basket":
					String[] assignBasketKeywords = {"assign-basket", "customer"};
					commandParser(command, assignBasketKeywords);
					storeModelService.assignBasket(wordMap.get("assign-basket"), wordMap.get("customer"), authToken);
					break;

				case "define-device":
					String[] deviceKeywords = {"define-device", "name", "type", "location"};
					commandParser(command, deviceKeywords);
					storeModelService.defineDevice(wordMap.get("define-device"), wordMap.get("name"),
							wordMap.get("type"), wordMap.get("location"), authToken);
					break;

				case "show-basket-items":
					storeModelService.showBasket(commandList[1].strip(), authToken);
					break;

				case "show-device":
					storeModelService.showDevice(commandList[1].strip(), authToken);
					break;

				case "get-device-location":
					return storeModelService.getDeviceLocation(commandList[1].strip(), authToken);

				case "get-nearest-speaker":
					return storeModelService.getNearestSpeaker(commandList[1].strip(), authToken);

				case "get-customer-basket":
					return storeModelService.getCustomerBasket(commandList[1].strip(), authToken);

				case "add-basket-item":
					String[] addBasketKeywords = {"add-basket-item", "product", "item_count"};
					commandParser(command, addBasketKeywords);
					storeModelService.addBasketItem(wordMap.get("add-basket-item"), wordMap.get("product"),
							Integer.parseInt(wordMap.get("item_count")), authToken);
					break;

				case "remove-basket-item":
					String[] removeBasketKeywords = {"remove-basket-item", "product", "item_count"};
					commandParser(command, removeBasketKeywords);
					storeModelService.removeBasketItem(wordMap.get("remove-basket-item"), wordMap.get("product"),
							Integer.parseInt(wordMap.get("item_count")), authToken);
					break;

				case "clear-basket":
					storeModelService.clearBasket(commandList[1].strip(), authToken);
					break;

				case "get-basket-cost":
					return storeModelService.getBasketCost(commandList[1].strip(), authToken);

				case "create-command":
					String[] createCommandKeywords = {"create-command", "message"};
					commandParser(command, createCommandKeywords);
					storeModelService.createCommand(wordMap.get("create-command"), wordMap.get("message"), authToken);
					break;

				case "create-event":
					String[] sensorEventKeywords = {"create-event", "store-location", "event"};
					commandParser(command, sensorEventKeywords);
					String str = wordMap.get("event").replaceAll("[\"“”]", "");
					String[] eventArgs = str.split(" ");
					storeModelService.createEvent(wordMap.get("create-event"), wordMap.get("store-location"), eventArgs, authToken);
					break;

				case "get-available-robot":
					return storeModelService.getAvailableRobot(commandList[1], authToken);

				case "get-appliance-list":
					return storeModelService.getAppliancesOfType(commandList[1].strip(), commandList[2], authToken);

				case "define_permission":
					String[] definePermissionKeywords = {"define_permission", "name", "description"};
					commandParser(command, definePermissionKeywords);
					authService.createPermission(wordMap.get("define_permission"), wordMap.get("name"), wordMap.get("description"));
					break;

				case "define_role":
					String[] defineRoleKeywords = {"define_role", "name", "description"};
					commandParser(command, defineRoleKeywords);
					authService.createRole(wordMap.get("define_role"), wordMap.get("name"), wordMap.get("description"));
					break;

				case "add_permission_to_role":
					authService.addRolePermission(commandList[1], commandList[2]);
					break;

				case "create_user":
					String[] createUserKeywords = {"create_user", "name"};
					commandParser(command, createUserKeywords);
					authService.createUser(wordMap.get("create_user"), wordMap.get("name"));
					break;

				case "add_user_credential":
					String[] addUserCredentialKeywords = {"add_user_credential", "type", "value"};
					commandParser(command, addUserCredentialKeywords);
					CredentialType credentialType;
					String type = wordMap.get("type");
					if (type.equals("voice_print")) credentialType = CredentialType.VOICE_PRINT;
					else if (type.equals("face_print")) credentialType = CredentialType.FACE_PRINT;
					else if (type.equals("password")) credentialType = CredentialType.PASSWORD;
					else throw new CommandProcessorException("add_user_credential", "Invalid credential type.", 0);
					authService.addUserCredential(wordMap.get("add_user_credential"), credentialType, wordMap.get("value"));
					break;

				case "add_role_to_user":
					authService.addUserRole(commandList[1], commandList[2]);
					break;

				case "create_resource_role":
					String[] resourceRoleKeywords = {"create_resource_role", "roleID", "resourceID"};
					commandParser(command, resourceRoleKeywords);
					authService.createResourceRole(wordMap.get("create_resource_role"), wordMap.get("roleID"), wordMap.get("resourceID"));
					break;

				case "add_resource_role_to_user":
					authService.addUserResourceRole(commandList[1], commandList[2]);
					break;

				case "display_authentication_inventory":
					authService.displayInventory();
					break;

				default:
					System.out.println("Invalid command: " + commandList[0]);
			}
		} catch (StoreModelServiceException e) {
			System.out.println("***ERROR*** \n" + e.getAction());
			System.out.println(e.getReason() + "\n***********");
			throw new CommandProcessorException(command, "StoreModelServiceException", 0);
		} catch (AuthenticationException e) {
			System.out.println("***ERROR*** \n" + e.getMessage());
			System.out.println("\n***********");
        }
        return "";
	}

	/**
	 * Reads lines from a given file object. Lines starting with a hash symbol are considered comments
	 * and will not be sent to the processCommand method.
	 * @param file  the script file containing one or many commands separated by newlines.
	 */
	public void processCommandFile(File file, String authToken) {
		Scanner reader;
		try {
			reader = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		int lineNumber = 0;
		// Read through the entire file
		while (reader.hasNextLine()) {
			lineNumber++;
			String data = reader.nextLine();

			// Skip empty lines
			if (data.isEmpty()) {
				continue;
			}
			else if (data.charAt(0) == '#') {
				// Print comments for output readability but don't try to process them
				System.out.println(data);
				continue;
			}

			try {
				// Send commands to processCommand method to be interpreted and acted on.
				processCommand(data.strip(), authToken);
			} catch (CommandProcessorException e) {
				System.out.println("***ERROR*** \n" + e.getCommand());
				System.out.println(e.getReason());
				System.out.println("Line number: " + e.getLineNumber() + "\n***********");
			}
		}
		reader.close();
	}

	/**
	 * Parses the given command string. Splits the command into substrings based on the provided command
	 * keywords to separate out the keywords from the variable strings.
	 * @param command   The single-line command string read from the CLI or script file.
	 * @param keyWords  The keywords associated with the command. If the command has the structure
	 *                  create-ledger <name> description <description> seed <seed>, the angle brackets are the
	 *                  variable strings and the keywords are 'create-ledger', 'description', and 'seed'.
	 * @return          A hashmap of paired strings where the key is the keyword and the value is the variable string.
	 */
	public void commandParser(String command, String[] keyWords) {
		wordMap.clear();
		int length = keyWords.length;
		for (int i = 0; i < length; i++) {
			// Split the command string into a list of substrings separated by the keyword
			String[] substrings = command.split(keyWords[i], 2);
			if (i == 0) {
				// The command string starts with the first keyword; after removing it, continue
				command = substrings[1];
				continue;
			}
			// substrings[0] contains the input for the previous keyword; substrings[1] contains the remaining string
			String str = substrings[0].strip().replaceAll("[\"“”]", "");
			try {
				command = substrings[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new CommandProcessorException(command, "Command syntax error, array out of bounds.", -1);
			}
			// Add keyword and associated input to map
			wordMap.put(keyWords[i-1], str);
		}
		// Add the last word pair to the list.
		wordMap.put(keyWords[length - 1], command.strip().replaceAll("[\"“”]", ""));
	}

	/**
	 * Throwable exception for invalid commands.
	 * Thrown by CommandProcessor.
	 */
	public static class CommandProcessorException extends RuntimeException {
		private final String command;
		private final String reason;
		private final int lineNumber;
		public CommandProcessorException(String command, String reason, int lineNumber) {
			super("StoreModelServiceException: " + command + " - " + reason);
			this.lineNumber = lineNumber;
			this.command = command;
			this.reason = reason;
		}

		public String getCommand() {
			return command;
		}

		public String getReason() {
			return reason;
		}

		public int getLineNumber() {
			return lineNumber;
		}
	}
}