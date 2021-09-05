package br.com.yallandev.potepvp.utils;

public class ChatAPI {

	public enum ChatState {
		ENABLED, DISABLED, PAYMENT;
	}

	private ChatState chatState;

	public ChatAPI() {
		this.chatState = ChatState.ENABLED;
	}

	public ChatState getChatState() {
		return chatState;
	}

	public void setChatState(ChatState chatState) {
		this.chatState = chatState;
	}

}
