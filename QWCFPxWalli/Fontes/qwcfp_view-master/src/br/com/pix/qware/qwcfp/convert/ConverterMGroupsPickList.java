package br.com.pix.qware.qwcfp.convert;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.RuleDTO;

@FacesConverter(value = "converterMGroupsPickList")
public class ConverterMGroupsPickList implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return getObjectFromUIPickListComponent(component, value);
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) {
		String string = null;
		if (object == null) {
			string = "";
		} else {
			try {
				string = String.valueOf(((GroupDTO) object).getGroupId());
			} catch (ClassCastException cce) {
				cce.printStackTrace();
			}
		}
		return string;
	}

	@SuppressWarnings("unchecked")
	private GroupDTO getObjectFromUIPickListComponent(UIComponent component, String value) {
		final DualListModel<GroupDTO> dualList;
		try {
			dualList = (DualListModel<GroupDTO>) ((PickList) component).getValue();
			GroupDTO rule = getObjectFromList(dualList.getSource(), Integer.valueOf(value));
			if (rule == null) {
				rule = getObjectFromList(dualList.getTarget(), Integer.valueOf(value));
			}

			return rule;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		
		return null;
	}

	private GroupDTO getObjectFromList(final List<?> list, final Integer identifier) {
		for (final Object object : list) {
			final GroupDTO group = (GroupDTO) object;
			if (group.getGroupId().equals(identifier)) {
				return group;
			}
		}
		return null;
	}
}
