package br.com.pix.qware.qwcfp.wrappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.qwcss.ws.dto.BoardItemDTO;
import br.com.qwcss.ws.dto.BoardStatusDTO;
import br.com.qwcss.ws.dto.DBoardFilesDTO;
import br.com.qwcss.ws.dto.DBoardStatusDTO;

public class DBoardFilesWrapper {

	private static final String RECEBIDO_STATUS = "RECEBIDO";

	private static final String PROCESSADO_STATUS = "PROCESSADO";

	private static final String DISPONIBILIZADO_STATUS = "DISPONIBILIZADO";

	private static final String ENFILEIRADO_STATUS = "ENFILEIRADO";

	private static final String GREEN_CLASS = "formDB_01"; // atual
	private static final String BLUE_CLASS =  "formDB_02";  // ja passou 
	private static final String GRAY_CLASS =  "formDB_03";  // nao passou 
	

	private String fileName;

	private String received;
	private String queued;
	private String available;
	private String processed;

	private Date receivedDate;
	private Date queuedDate;
	private Date availableDate;
	private Date processedDate;

	private String receivedIp;
	private String queuedIp;
	private String availableIp;
	private String processedIp;

	public DBoardFilesWrapper() {
	}

	public DBoardFilesWrapper(BoardItemDTO dashFile) {
		super();


		List<BoardStatusDTO> statuses = null;
		BoardStatusDTO[] statusList = dashFile.getStatusList();
		if (statusList != null) {
			statuses = Arrays.asList(statusList);
		}else {
			statuses = new ArrayList<>();
		}
		
		this.fileName = dashFile.getFileName();

		this.received = GRAY_CLASS;
		this.queued = GRAY_CLASS;
		this.available = GRAY_CLASS;
		this.processed = GRAY_CLASS;

		HashMap<Integer, Boolean> colorMap = new HashMap<Integer, Boolean>();

		if (statuses != null) {
			for (BoardStatusDTO dBoardStatusDTO : statuses) {
				if (dBoardStatusDTO.getStatus().equalsIgnoreCase(RECEBIDO_STATUS)) {
					colorMap.put(1, true);
					this.receivedDate = dBoardStatusDTO.getEventDate().getTime();
					this.receivedIp = dBoardStatusDTO.getIpAddress();

				} else if (dBoardStatusDTO.getStatus().equalsIgnoreCase(ENFILEIRADO_STATUS)) {
					colorMap.put(2, true);
					this.queuedDate = dBoardStatusDTO.getEventDate().getTime();;
					this.queuedIp = dBoardStatusDTO.getIpAddress();

				} else if (dBoardStatusDTO.getStatus().equalsIgnoreCase(DISPONIBILIZADO_STATUS)) {
					colorMap.put(3, true);
					this.availableDate = dBoardStatusDTO.getEventDate().getTime();
					this.availableIp = dBoardStatusDTO.getIpAddress();

				} else if (dBoardStatusDTO.getStatus().equalsIgnoreCase(PROCESSADO_STATUS)) {
					colorMap.put(4, true);
					this.processedDate = dBoardStatusDTO.getEventDate().getTime();
					this.processedIp = dBoardStatusDTO.getIpAddress();

				}
			}
		}

		for (Integer step : colorMap.keySet()) {

			int size = colorMap.size();
			Boolean value = colorMap.get(step);
			
			if (value == null)
				value = false;

			if (size != step && value) {
				colorSwitch(step, GREEN_CLASS );
			} else {
				if (value) {
					colorSwitch(step, BLUE_CLASS );
				}
			}

		}

	}

	private void colorSwitch(Integer step, String colorClass) {
		switch (step) {
		case 1:
			this.received = colorClass;
			break;
		case 2:
			this.queued = colorClass;
			break;
		case 3:
			this.available = colorClass;
			break;
		case 4:
			this.processed = colorClass;
			break;
		default:
			break;
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public String getQueued() {
		return queued;
	}

	public void setQueued(String queued) {
		this.queued = queued;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getQueuedDate() {
		return queuedDate;
	}

	public void setQueuedDate(Date queuedDate) {
		this.queuedDate = queuedDate;
	}

	public Date getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public String getReceivedIp() {
		return receivedIp;
	}

	public void setReceivedIp(String receivedIp) {
		this.receivedIp = receivedIp;
	}

	public String getQueuedIp() {
		return queuedIp;
	}

	public void setQueuedIp(String queuedIp) {
		this.queuedIp = queuedIp;
	}

	public String getAvailableIp() {
		return availableIp;
	}

	public void setAvailableIp(String availableIp) {
		this.availableIp = availableIp;
	}

	public String getProcessedIp() {
		return processedIp;
	}

	public void setProcessedIp(String processedIp) {
		this.processedIp = processedIp;
	}

}
