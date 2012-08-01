package my.projects.contactbook.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


	  class ExceptionDialogBox extends DialogBox implements ClickListener {
		  public ExceptionDialogBox() {
			
		    setText("No contact is selected");
		    
		    InlineLabel message=new InlineLabel("Please choose the contact to make changes!");
		     VerticalPanel vp=new VerticalPanel();
		  Button ok=new Button("OK",new ClickListener() {
			
			@Override
			@Deprecated
			public
			void onClick(Widget sender) {
				// TODO Auto-generated method stub
				hide();
			}
		});
		  DOM.setElementAttribute(ok.getElement(), "id", "ok");
		    vp.add(message);
		    vp.add(ok);
		    setWidget(vp);
			  
		  }
		  
		  
		@Override
		@Deprecated
		public
		void onClick(Widget sender) {
			// TODO Auto-generated method stub
			hide();
		}
		  
		  
	  }
