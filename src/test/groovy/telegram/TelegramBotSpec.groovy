import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import org.telegram.telegrambots.api.objects.Update
import spock.lang.Specification
import telegram.TelegramBot

class TelegramBotSpec extends Specification {

    @Subject
    TelegramBot telegramBot = new TelegramBot();

    @Collaborator
    Update update = Stub();

    @Collaborator
    Message message = Stub();

    @Collaborator
    SendMessage sendMessageRequest = Mock();

    def "when update has no message nothing should be return to the user"() {

        given: "an update request with no message"
        update.hasMessage() >> false

        when: "onUpdateReceived is called"
        telegramBot.onUpdateReceived(update)

        then: "no message should be return to the user"
        0 * sendMessageRequest.setText(_ as String)
    }

    def "when update has message '/start' then a greeting should be send to the user"() {

        given: "an update request with message '/start' "
        update.hasMessage() >> true
        update.getMessage() >> message
        message.hasText() >> true
        message.getText() >> "/start"

        when: "onUpdateReceived is called"
        telegramBot.onUpdateReceived(update)

        then: "a greeting will be sent back to the user"
        1 * sendMessageRequest.setText("Hi trainer." + TelegramBot.newline + "Use one of the commands to get information")

        and: "expect error since we are not sending the message when testing"
        thrown(NullPointerException)

    }

    def "when update has a message that is not a command"(){

        given: "an update request with a message that is not a command"
        update.hasMessage() >> true
        update.getMessage() >> message
        message.hasText() >> true
        message.getText() >> "/not_a_valid_command"

        when: "onUpdateReceived is called"
        telegramBot.onUpdateReceived(update)

        then: "a message will ask the user to use a command"
        1 * sendMessageRequest.setText("Please use of the commands");

        and: "expect error since we are not sending the message when testing"
        thrown(NullPointerException)

    }

    def "when update has a valid command"(){

        given: "an update request with a valid message"
        update.hasMessage() >> true
        update.getMessage() >> message
        message.hasText() >> true
        message.getText() >> "/pikachu"

        when: "onUpdateReceived is called"
        telegramBot.onUpdateReceived(update)

        then: "information about the pokemon is sent"
        0 * sendMessageRequest.setText("Number " + _ as String);

        and: "expect error since we are not sending the message when testing"
        thrown(NullPointerException)

    }


}

