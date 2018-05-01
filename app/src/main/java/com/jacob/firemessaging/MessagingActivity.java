package com.jacob.firemessaging;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessagingActivity extends AppCompatActivity implements ChildEventListener{

    /**
     * Database reference object to point to msg node in Firebase
     */
    public static final String MESSAGES_FIREBASE_KEY = "messages";

    /**
     * This is a reference to the root of our Firebase. With this object, we can access any child
     * information in the database.
     */
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    /**
     * Using the key, "messages", we can access a reference to the list of messages. We will be
     * listening to changes to the children of this reference in this Activity.
     */
    private DatabaseReference messagesReference = firebase.getReference(MESSAGES_FIREBASE_KEY);

    private RecyclerView messagesList;
    private MessageAdapter messageAdapter;

    private EditText messageEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        messagesList = (RecyclerView) findViewById(R.id.messages_list);
        messagesList.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false));

        messageEntry = (EditText) findViewById(R.id.message_entry);
        messageEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean enterWasPressed = event == null
                        || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN);
                if (enterWasPressed) {
                    sendMessage(null);
                    return true;
                }

                return false;
            }
        });

        messageAdapter = new MessageAdapter();
        messagesList.setAdapter(messageAdapter);

        /*
         * Since MessagingActivity implement the ChildEventListener interface, we need to set the
         * ChildEventListener to this instance of the MessagingActivity. If we don't do this, we
         * won't be notified by Firebase when children of our messaging reference in the database
         * have changed.
         */
        messagesReference.addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message rMessage = dataSnapshot.getValue(Message.class);
        messageAdapter.addMessage(rMessage);

        scrollToMostRecentMessage();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    /**
     * Called when the "Send Message" button is clicked.
     *
     * @param view The button that was clicked.
     */
    public void sendMessage(View view) {
        pushMessageToFirebase();

        resetMessageEntry();

        hideKeyboard();
    }

    /**
     * Sends a message to Firebase
     */
    private void pushMessageToFirebase() {
        String messageContent = messageEntry.getText().toString();
        String username = UserClass.getUsername(this);
        long now = System.currentTimeMillis();
        Message messageToSend = new Message(username, messageContent, now);
        messagesReference.push().setValue(messageToSend);
    }

    /**
     * Hides the soft keyboard. Used after a message is sent.
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(messageEntry.getWindowToken(), 0);
    }

    /**
     * Clears the entered text. Used after a message is sent.
     */
    private void resetMessageEntry() {
        messageEntry.setText("");
    }

    /**
     * To give a nice effect when entering into this Activity, we can use this method to scroll
     * to the bottom of the list of messages. It is also used when a new message is sent.
     */
    private void scrollToMostRecentMessage() {
        int mostRecentMessageIndex = messageAdapter.getItemCount() - 1;
        messagesList.smoothScrollToPosition(mostRecentMessageIndex);
    }

    /**
     * Starts this method after login
     *
     * @param context Used to start Activity
     */
    public static void start(Context context) {
        Intent startMessagingActivity = new Intent(context, MessagingActivity.class);
        context.startActivity(startMessagingActivity);
    }


}