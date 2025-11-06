package org.leedsmet.studentresearch.jmx.assembler;

import java.util.ArrayList;
import java.util.List;
import javax.management.Descriptor;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

/**
 * @author Eamonn McManus
 * 
 */
public class NoGetterMBeanInfo extends ModelMBeanInfoSupport {
	
	private static final String ROLE = "role";

	private static final String SETTER = "setter";

	private static final String GETTER = "getter";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoGetterMBeanInfo(ModelMBeanInfo mmbi) {
		super(mmbi);
	}

	@Override
	public NoGetterMBeanInfo clone() {
		return new NoGetterMBeanInfo(this);
	}

	private Object writeReplace() {
		List<ModelMBeanOperationInfo> ops = new ArrayList<ModelMBeanOperationInfo>();
		for (MBeanOperationInfo mboi : this.getOperations()) {
			ModelMBeanOperationInfo mmboi = (ModelMBeanOperationInfo) mboi;
			Descriptor d = mmboi.getDescriptor();
			String role = (String) d.getFieldValue(NoGetterMBeanInfo.ROLE);
			if (!NoGetterMBeanInfo.GETTER.equalsIgnoreCase(role)
					&& !NoGetterMBeanInfo.SETTER.equalsIgnoreCase(role))
				ops.add(mmboi);
		}
		ModelMBeanOperationInfo[] mbois = new ModelMBeanOperationInfo[ops
				.size()];
		ops.toArray(mbois);
		Descriptor mbeanDescriptor;
		try {
			mbeanDescriptor = this.getMBeanDescriptor();
		} catch (MBeanException e) {
			throw new RuntimeException(e);
		}
		return new ModelMBeanInfoSupport(this.getClassName(), this
				.getDescription(), (ModelMBeanAttributeInfo[]) this
				.getAttributes(), (ModelMBeanConstructorInfo[]) this
				.getConstructors(), mbois, (ModelMBeanNotificationInfo[]) this
				.getNotifications(), mbeanDescriptor);
	}
}
