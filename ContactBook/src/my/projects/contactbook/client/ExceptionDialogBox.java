package my.projects.contactbook.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class ExceptionDialogBox extends DialogBox implements ClickHandler {
		  public ExceptionDialogBox() {
			setText("No contact is selected");
		    InlineLabel message=new InlineLabel("Please choose the contact to make changes!");
		    VerticalPanel vp=new VerticalPanel();
		    Button ok=new Button("OK",this);
		    DOM.setElementAttribute(ok.getElement(), "id", "ok");
		    vp.add(message);
		    vp.add(ok);
		    setWidget(vp);
			  
		  }

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			hide();
		}
  
	  }
