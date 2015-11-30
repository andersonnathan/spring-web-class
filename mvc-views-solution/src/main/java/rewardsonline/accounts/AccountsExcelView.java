package rewardsonline.accounts;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * This view generates a spreadsheet from Account objects.
 */
public class AccountsExcelView extends AbstractExcelView {

	// The ISO standard date format is Year then Month then Day.
	public static final String ISO_DATE_FORMAT = "yy/m/d";

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Get the list of accounts from the model
		List<Account> accounts = (List<Account>) model.get("accountList");

		// Create a new style to define the date-format to use
		HSSFCellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setDataFormat(HSSFDataFormat
				.getBuiltinFormat(ISO_DATE_FORMAT));

		HSSFSheet sheet = workbook.createSheet();

		// Create a new row for each account in the list. Create columns
		// for name (account owner), number and account owner's date of birth.
		for (short i = 0; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			HSSFRow row = sheet.createRow(i);
			addStringCell(row, 0, account.getName());
			addStringCell(row, 1, account.getNumber());
			addDateCell(row, 2, account.getDateOfBirth(), dateStyle);
		}
	}

	/**
	 * Create a new cell to contain a string value.
	 * 
	 * @param row
	 *            The row to add the cell to.
	 * @param index
	 *            The column in the row.
	 * @param value
	 *            The new cell value.
	 * @return The newly created and initialized cell.
	 */
	private HSSFCell addStringCell(HSSFRow row, int index, String value) {
		HSSFCell cell = row.createCell((short) index);
		if (value != null)
			cell.setCellValue(new HSSFRichTextString(value));
		return cell;
	}

	/**
	 * Create a new cell to contain a date.
	 * 
	 * @param row
	 *            The row to add the cell to.
	 * @param index
	 *            The column in the row.
	 * @param value
	 *            The new cell value.
	 * @param dateStyle
	 *            The date format to use.
	 * 
	 * @return The newly created and initialized cell.
	 */
	private HSSFCell addDateCell(HSSFRow row, int index, Date date,
			HSSFCellStyle dateStyle) {
		HSSFCell cell = row.createCell((short) index);

		if (date != null) {
			cell.setCellValue(date);
			cell.setCellStyle(dateStyle);
		}

		return cell;
	}

}
