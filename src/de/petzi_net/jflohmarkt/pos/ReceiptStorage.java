/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import de.petzi_net.jflohmarkt.pos.xml.Receipt;
import de.petzi_net.jflohmarkt.pos.xml.State;

/**
 * @author axel
 *
 */
public class ReceiptStorage {
	
	private final static String POS_NUMBER = "pos.number";
	private final static String LAST_RECEIPT_NUMBER = "last.receipt.number";
	
	private final DatatypeFactory datatypeFactory;
	private final Marshaller marshaller;
	
	private int posNumber;
	private int lastReceiptNumber;
	private int cashierNumber;
	
	public ReceiptStorage() {
		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
		try {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("de/petzi_net/jflohmarkt/pos/xml/jflohmarktpos.xsd"));
			JAXBContext jaxbContext = JAXBContext.newInstance(Receipt.class.getPackage().getName());
			this.marshaller = jaxbContext.createMarshaller();
			this.marshaller.setSchema(schema);
			this.marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		File settingsFile = getSettingsFile();
		if (settingsFile.exists()) {
			if (!settingsFile.isFile() || !settingsFile.canRead())
				throw new IllegalStateException("cannot read settings file: " + settingsFile);
			Properties settings = new Properties();
			try {
				Reader reader = new FileReader(settingsFile);
				settings.load(reader);
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try {
				this.posNumber = Integer.valueOf(settings.getProperty(POS_NUMBER, "0"));
			} catch (NumberFormatException e) {
				this.posNumber = 0;
			}
			try {
				this.lastReceiptNumber = Integer.valueOf(settings.getProperty(LAST_RECEIPT_NUMBER, "0"));
			} catch (NumberFormatException e) {
				this.lastReceiptNumber = 0;
			}
		} else {
			this.posNumber = 0;
			this.lastReceiptNumber = 0;
		}
		this.cashierNumber = 0;
	}
	
	public XMLGregorianCalendar getCurrentTime() {
		return datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar());
	}
	
	public <T extends Receipt> T createReceipt(Class<T> receiptType) {
		T result;
		try {
			result = receiptType.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		result.setPos(posNumber);
		result.setReceipt(++lastReceiptNumber);
		result.setTimestamp(getCurrentTime());
		result.setCashier(cashierNumber);
		result.setState(State.ACTIVE);
		storeReceipt(result, false);
		setup(posNumber, lastReceiptNumber);
		return result;
	}
	
	public void storeReceipt(Receipt receipt) {
		storeReceipt(receipt, true);
	}
	
	private void storeReceipt(Receipt receipt, boolean overwrite) {
		receipt.setTimestamp(getCurrentTime());
		File receiptFile = new File(getDataFolder(), "receipt_" + receipt.getPos() + "_" + receipt.getReceipt() + ".xml");
		if (receiptFile.exists() && !overwrite)
			throw new IllegalStateException("receipt file already exists: " + receiptFile);
		try {
			marshaller.marshal(receipt, receiptFile);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getPosNumber() {
		return posNumber;
	}
	
	public int getLastReceiptNumber() {
		return lastReceiptNumber;
	}
	
	public int getCashierNumber() {
		return cashierNumber;
	}
	
	public void setCashierNumber(int cashierNumber) {
		this.cashierNumber = cashierNumber;
	}
	
	public void setup(int posNumber, int lastReceiptNumber) {
		Properties settings = new Properties();
		settings.setProperty(POS_NUMBER, Integer.toString(posNumber));
		settings.setProperty(LAST_RECEIPT_NUMBER, Integer.toString(lastReceiptNumber));
		File settingsFile = getSettingsFile();
		try {
			Writer writer = new FileWriter(settingsFile);
			settings.store(writer, null);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.posNumber = posNumber;
		this.lastReceiptNumber = lastReceiptNumber;
	}
	
	private File getSettingsFile() {
		return new File(getDataFolder(), "settings.properties");
	}
	
	private File getDataFolder() {
		File result = new File(System.getProperty("user.home"), ".jflohmarktpos");
		if (!result.exists())
			result.mkdirs();
		if (!result.isDirectory())
			throw new IllegalStateException("cannot use data folder: " + result);
		return result;
	}

}
