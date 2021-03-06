package org.better.swing.builders;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.JTextField;
import org.better.swing.builders.util.guava.Preconditions;
import org.better.swing.builders.util.guava.Strings;
import org.better.swing.builders.util.swing.SwingActionHelper;

/**
 * Builder class for building swing text fields. Example usage:
 * <pre>{@code
 *   JTextField field = JTextFieldBuilder.builder()
 *      .columns(15)
 *      .text("initial text")
 *      .actionListener(textValue -> enterPressedWithTextInput(textValue)
 *      .keyTypedListener(textValue -> processKeyTypedWithText(textValue)
 *      .compositeBuilder()
 *      .withButton("buttonText", textValue -> buttonPressedAction(textValue)
 *      .build();
 * }</pre>
 */
public class JTextFieldBuilder {

  private String text;
  private int columns;

  private Consumer<String> textEnteredAction;
  private boolean enabled = true;


  private JTextFieldBuilder() {

  }

  public static JTextFieldBuilder builder() {
    return new JTextFieldBuilder();
  }

  /**
   * Adds a listener action to the text field that is fired whenever a key is typed.
   */
  public static void keyTypedListener(final Consumer<KeyEvent> listener,
      final JTextField... fields) {
    Preconditions.checkNotNull(listener);
    Preconditions.checkArgument(fields.length > 0);
    Preconditions.checkNotNull(fields[0]);

    Arrays.asList(fields)
        .forEach(field -> field.addKeyListener(SwingActionHelper.keyReleaseListener(listener)));
  }

  /**
   * Sets the initial value displayed on the text field.
   */
  public JTextFieldBuilder text(final String value) {
    Preconditions.checkNotNull(value);
    this.text = value;
    return this;
  }

  /**
   * Convenience method for setting the text value to a numeric value.
   */
  public JTextFieldBuilder text(final int value) {
    this.text = String.valueOf(value);
    return this;
  }

  /**
   * Defines the width of the text field. Value is passed directly to swing.
   * TODO: list some typical/reasonable value examples
   */
  public JTextFieldBuilder columns(final int columns) {
    Preconditions.checkArgument(columns > 0);
    this.columns = columns;
    return this;
  }

  /**
   * Builds the swing component.
   */
  public JTextField build() {
    final JTextField textField = new JTextField(Strings.nullToEmpty(this.text));
    if (columns > 0) {
      textField.setColumns(columns);
    }

    if (textEnteredAction != null) {
      textField.addActionListener(e -> textEnteredAction.accept(textField.getText()));
    }

    textField.setEnabled(enabled);
    return textField;
  }

  /**
   * Adds an action listener that is fired when the user presses enter after entering text
   * into the text field.
   *
   * @param textEnteredAction Action to fire on 'enter', input value is the current value of the
   * text field.
   */
  public JTextFieldBuilder actionListener(final Consumer<String> textEnteredAction) {
    Preconditions.checkNotNull(textEnteredAction);
    this.textEnteredAction = textEnteredAction;
    return this;
  }

  /**
   * Toggles whether the text field is 'enabled', if 'disabled' then it is read only.
   * By default text fields will be enabled.
   */
  public JTextFieldBuilder enabled(final boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Disables a text field, sets it to 'read-only'.
   * Same as calling 'builder().enabled(false)'
   */
  public JTextFieldBuilder disabled() {
    this.enabled = false;
    return this;
  }

}
