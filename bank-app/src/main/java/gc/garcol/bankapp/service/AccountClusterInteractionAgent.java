package gc.garcol.bankapp.service;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.MessageHandler;

public class AccountClusterInteractionAgent implements Agent, MessageHandler {

  @Override
  public void onMessage(int msgTypeId, MutableDirectBuffer buffer, int index, int length) {

  }

  @Override
  public int doWork() throws Exception {
    return 0;
  }

  @Override
  public String roleName() {
    return null;
  }
}
