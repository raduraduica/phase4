/*
 * Copyright (C) 2015-2022 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phase4.model.pmode.leg;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.state.ETriState;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

/**
 * PMode reliability stuff.
 *
 * @author Philip Helger
 */
@NotThreadSafe
@MustImplementEqualsAndHashcode
public class PModeLegReliability implements Serializable
{
  public static final boolean DEFAULT_TERMINATE_GROUP = false;
  public static final boolean DEFAULT_START_GROUP = false;
  public static final boolean DEFAULT_IN_ORDER_CONTACT = false;
  public static final boolean DEFAULT_AT_MOST_ONCE_CONTRACT = false;
  public static final boolean DEFAULT_AT_LEAST_ONCE_CONTRACT_ACK_RESPONSE = false;
  public static final boolean DEFAULT_AT_LEAST_ONCE_CONTRACT = false;
  public static final boolean DEFAULT_AT_LEAST_ONCE_ACK_ON_DELIVERY = false;

  /**
   * If "true", this Boolean parameter indicates that the "At-Least-Once"
   * reliability contract (see Section 8.2.2) is to be used between MSH and
   * Consumer (Guaranteed Delivery). It also indicates that this contract
   * applies to ebMS signals (see Section 8.2.1) (e.g. PullRequest) between the
   * receiving reliability module and the next MSH component (e.g. RM-Consumer).
   */
  private ETriState m_eAtLeastOnceContract = ETriState.UNDEFINED;

  /**
   * This Boolean parameter indicates the semantics of acknowledgments that are
   * generated by the reliability module. It is usually constrained by the
   * implementation and not set by users. For User messages: if "true", the
   * acknowledgment is only sent after the message has been delivered by the MSH
   * to the Consumer entity (see Case 2 in Section 8.2.4). If "false", the only
   * guarantee for the sender when receiving an acknowledgment is that the User
   * message has been well received (see Case 1 or 3 in Section 8.2.4), and made
   * available for further processing within the MSH. For Signal messages - e.g.
   * PullRequest: if "true", indicates that Signal messages are acknowledged
   * only if delivered (see Section 8.2.1) from the receiving reliability module
   * to the next MSH component (Case 3 in Section 8.2.4), i.e. to the
   * RM-Consumer (see 8.1.2). If "false", the message acknowledgment only
   * guarantees receipt of the signal (Case 1 in Section 8.2.4).
   */
  private ETriState m_eAtLeastOnceAckOnDelivery = ETriState.UNDEFINED;

  /**
   * This parameter is a URI that specifies where acknowledgments are to be
   * sent. It may contain an anonymous URI (defined in WS-Addressing). If
   * absent, acknowledgments are to be sent to the same URI associated with the
   * MSH sending messages reliably.
   */
  private String m_sAtLeastOnceContractAcksTo;

  /**
   * This Boolean is true when an Acknowledgment must be sent, for a response
   * that is sent reliably.
   */
  private ETriState m_eAtLeastOnceContractAckResponse = ETriState.UNDEFINED;

  /**
   * This parameter indicates whether a reliability acknowledgment is to be sent
   * as a callback, synchronously in the response (back-channel of underlying
   * protocol), or as response of separate ack pulling. Three values are
   * possible for this parameter, when using WS-Reliability: "Response",
   * "Callback", or "Poll".
   */
  private String m_sAtLeastOnceReplyPattern;

  /**
   * If "true", this Boolean parameter indicates that "At-Most-Once" (or
   * duplicate elimination) should be enforced when receiving a message. The
   * contract is for delivery between MSH and Consumer for User messages (see
   * Section 8.2.2), and between reliability module and next MSH component for
   * Signal messages (see Section 8.2.1).
   */
  private ETriState m_eAtMostOnceContract = ETriState.UNDEFINED;

  /**
   * If "true", this Boolean parameter indicates that this message is part of an
   * ordered sequence. It only concerns User messages (delivery contract between
   * MSH and Consumer application, see Section 8.2.2).
   */
  private ETriState m_eInOrderContract = ETriState.UNDEFINED;

  /**
   * This parameter is a Boolean that may be used to indicate if messages
   * matching this P-Mode must be associated with a new reliability group or
   * sequence. For example, a particular Service and Action may have the
   * application semantics of initiating a new ordered sequence of messages.
   */
  private ETriState m_eStartGroup = ETriState.UNDEFINED;

  /**
   * This parameter tells how to correlate a message matching this P-Mode with
   * an existing reliability group or sequence. It is a comma-separated list of
   * XPath elements relative to the <code>eb:Messaging</code> header. Each one
   * of these XPaths identifies an element or attribute inside
   * <code>eb:UserMessage</code> or <code>eb:SignalMessage</code>, and may
   * include predicates. For example,
   * <code>"eb:UserMessage/eb:CollaborationInfo/eb:ConversationId,
   *  eb:UserMessage/eb:MessageProperties/eb:Property[@name=\"ProcessInstance\"]</code>
   * will correlate all messages that share the same <code>ConversationId</code>
   * and have the same value for the message property named
   * <code>ProcessInstance</code>. In case there is no ongoing group or sequence
   * associated with the values in Reliability.Correlation for a message under
   * this P-Mode, then a new group/sequence is started.
   */
  private final ICommonsList <String> m_aCorrelation = new CommonsArrayList <> ();

  /**
   * This parameter is a Boolean value that may be used to indicate if messages
   * matching this P-Mode must cause the closure of the reliability group or
   * sequence with which they correlate.
   */
  private ETriState m_eTerminateGroup = ETriState.UNDEFINED;

  public PModeLegReliability ()
  {}

  public PModeLegReliability (@Nonnull final ETriState eAtLeastOnceContract,
                              @Nonnull final ETriState eAtLeastOnceAckOnDelivery,
                              @Nullable final String sAtLeastOnceContractAcksTo,
                              @Nonnull final ETriState eAtLeastOnceContractAckResponse,
                              @Nullable final String sAtLeastOnceReplyPattern,
                              @Nonnull final ETriState eAtMostOnceContract,
                              @Nonnull final ETriState eInOrderContract,
                              @Nonnull final ETriState eStartGroup,
                              @Nullable final ICommonsList <String> aCorrelation,
                              @Nonnull final ETriState eTerminateGroup)
  {
    setAtLeastOnceContract (eAtLeastOnceContract);
    setAtLeastOnceAckOnDelivery (eAtLeastOnceAckOnDelivery);
    setAtLeastOnceContractAcksTo (sAtLeastOnceContractAcksTo);
    setAtLeastOnceContractAckResponse (eAtLeastOnceContractAckResponse);
    setAtLeastOnceReplyPattern (sAtLeastOnceReplyPattern);
    setAtMostOnceContract (eAtMostOnceContract);
    setInOrderContract (eInOrderContract);
    setStartGroup (eStartGroup);
    setCorrelation (aCorrelation);
    setTerminateGroup (eTerminateGroup);
  }

  public final boolean isAtLeastOnceContractDefined ()
  {
    return m_eAtLeastOnceContract.isDefined ();
  }

  public final boolean isAtLeastOnceContract ()
  {
    return m_eAtLeastOnceContract.getAsBooleanValue (DEFAULT_AT_LEAST_ONCE_CONTRACT);
  }

  @Nonnull
  public final EChange setAtLeastOnceContract (final boolean bAtLeastOnceContract)
  {
    return setAtLeastOnceContract (ETriState.valueOf (bAtLeastOnceContract));
  }

  @Nonnull
  public final EChange setAtLeastOnceContract (@Nonnull final ETriState eAtLeastOnceContract)
  {
    ValueEnforcer.notNull (eAtLeastOnceContract, "AtLeastOnceContract");
    if (eAtLeastOnceContract.equals (m_eAtLeastOnceContract))
      return EChange.UNCHANGED;
    m_eAtLeastOnceContract = eAtLeastOnceContract;
    return EChange.CHANGED;
  }

  public final boolean isAtLeastOnceAckOnDeliveryDefined ()
  {
    return m_eAtLeastOnceAckOnDelivery.isDefined ();
  }

  public final boolean isAtLeastOnceAckOnDelivery ()
  {
    return m_eAtLeastOnceAckOnDelivery.getAsBooleanValue (DEFAULT_AT_LEAST_ONCE_ACK_ON_DELIVERY);
  }

  @Nonnull
  public final EChange setAtLeastOnceAckOnDelivery (final boolean bAtLeastOnceAckOnDelivery)
  {
    return setAtLeastOnceAckOnDelivery (ETriState.valueOf (bAtLeastOnceAckOnDelivery));
  }

  @Nonnull
  public final EChange setAtLeastOnceAckOnDelivery (@Nonnull final ETriState eAtLeastOnceAckOnDelivery)
  {
    ValueEnforcer.notNull (eAtLeastOnceAckOnDelivery, "AtLeastOnceAckOnDelivery");
    if (eAtLeastOnceAckOnDelivery.equals (m_eAtLeastOnceAckOnDelivery))
      return EChange.UNCHANGED;
    m_eAtLeastOnceAckOnDelivery = eAtLeastOnceAckOnDelivery;
    return EChange.CHANGED;
  }

  @Nullable
  public final String getAtLeastOnceContractAcksTo ()
  {
    return m_sAtLeastOnceContractAcksTo;
  }

  public final boolean hasAtLeastOnceContractAcksTo ()
  {
    return StringHelper.hasText (m_sAtLeastOnceContractAcksTo);
  }

  @Nonnull
  public final EChange setAtLeastOnceContractAcksTo (@Nullable final String sAtLeastOnceContractAcksTo)
  {
    if (EqualsHelper.equals (sAtLeastOnceContractAcksTo, m_sAtLeastOnceContractAcksTo))
      return EChange.UNCHANGED;
    m_sAtLeastOnceContractAcksTo = sAtLeastOnceContractAcksTo;
    return EChange.CHANGED;
  }

  public final boolean isAtLeastOnceContractAckResponseDefined ()
  {
    return m_eAtLeastOnceContractAckResponse.isDefined ();
  }

  public final boolean isAtLeastOnceContractAckResponse ()
  {
    return m_eAtLeastOnceContractAckResponse.getAsBooleanValue (DEFAULT_AT_LEAST_ONCE_CONTRACT_ACK_RESPONSE);
  }

  @Nonnull
  public final EChange setAtLeastOnceContractAckResponse (final boolean bAtLeastOnceContractAckResponse)
  {
    return setAtLeastOnceContractAckResponse (ETriState.valueOf (bAtLeastOnceContractAckResponse));
  }

  @Nonnull
  public final EChange setAtLeastOnceContractAckResponse (@Nonnull final ETriState eAtLeastOnceContractAckResponse)
  {
    ValueEnforcer.notNull (eAtLeastOnceContractAckResponse, "AtLeastOnceContractAckResponse");
    if (eAtLeastOnceContractAckResponse.equals (m_eAtLeastOnceContractAckResponse))
      return EChange.UNCHANGED;
    m_eAtLeastOnceContractAckResponse = eAtLeastOnceContractAckResponse;
    return EChange.CHANGED;
  }

  @Nullable
  public final String getAtLeastOnceReplyPattern ()
  {
    return m_sAtLeastOnceReplyPattern;
  }

  public final boolean hasAtLeastOnceReplyPattern ()
  {
    return StringHelper.hasText (m_sAtLeastOnceReplyPattern);
  }

  @Nonnull
  public final EChange setAtLeastOnceReplyPattern (@Nullable final String sAtLeastOnceReplyPattern)
  {
    if (EqualsHelper.equals (sAtLeastOnceReplyPattern, m_sAtLeastOnceReplyPattern))
      return EChange.UNCHANGED;
    m_sAtLeastOnceReplyPattern = sAtLeastOnceReplyPattern;
    return EChange.CHANGED;
  }

  public final boolean isAtMostOnceContractDefined ()
  {
    return m_eAtMostOnceContract.isDefined ();
  }

  public final boolean isAtMostOnceContract ()
  {
    return m_eAtMostOnceContract.getAsBooleanValue (DEFAULT_AT_MOST_ONCE_CONTRACT);
  }

  @Nonnull
  public final EChange setAtMostOnceContract (final boolean bAtMostOnceContract)
  {
    return setAtMostOnceContract (ETriState.valueOf (bAtMostOnceContract));
  }

  @Nonnull
  public final EChange setAtMostOnceContract (@Nonnull final ETriState eAtMostOnceContract)
  {
    ValueEnforcer.notNull (eAtMostOnceContract, "AtMostOnceContract");
    if (eAtMostOnceContract.equals (m_eAtMostOnceContract))
      return EChange.UNCHANGED;
    m_eAtMostOnceContract = eAtMostOnceContract;
    return EChange.CHANGED;
  }

  public final boolean isInOrderContractDefined ()
  {
    return m_eInOrderContract.isDefined ();
  }

  public final boolean isInOrderContract ()
  {
    return m_eInOrderContract.getAsBooleanValue (DEFAULT_IN_ORDER_CONTACT);
  }

  @Nonnull
  public final EChange setInOrderContract (final boolean bInOrderContract)
  {
    return setInOrderContract (ETriState.valueOf (bInOrderContract));
  }

  @Nonnull
  public final EChange setInOrderContract (@Nonnull final ETriState eInOrderContract)
  {
    ValueEnforcer.notNull (eInOrderContract, "InOrderContract");
    if (eInOrderContract.equals (m_eInOrderContract))
      return EChange.UNCHANGED;
    m_eInOrderContract = eInOrderContract;
    return EChange.CHANGED;
  }

  public final boolean isStartGroupDefined ()
  {
    return m_eStartGroup.isDefined ();
  }

  public final boolean isStartGroup ()
  {
    return m_eStartGroup.getAsBooleanValue (DEFAULT_START_GROUP);
  }

  @Nonnull
  public final EChange setStartGroup (final boolean bStartGroup)
  {
    return setStartGroup (ETriState.valueOf (bStartGroup));
  }

  @Nonnull
  public final EChange setStartGroup (@Nonnull final ETriState eStartGroup)
  {
    ValueEnforcer.notNull (eStartGroup, "StartGroup");
    if (eStartGroup.equals (m_eStartGroup))
      return EChange.UNCHANGED;
    m_eStartGroup = eStartGroup;
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsMutableObject
  public final ICommonsList <String> correlations ()
  {
    return m_aCorrelation;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final ICommonsList <String> getAllCorrelations ()
  {
    return m_aCorrelation.getClone ();
  }

  @Nonnull
  public final EChange setCorrelation (@Nullable final ICommonsList <String> aCorrelation)
  {
    // Ensure same type
    if (EqualsHelper.equals (aCorrelation, m_aCorrelation))
      return EChange.UNCHANGED;
    m_aCorrelation.setAll (aCorrelation);
    return EChange.CHANGED;
  }

  public final boolean isTerminateGroupDefined ()
  {
    return m_eTerminateGroup.isDefined ();
  }

  public final boolean isTerminateGroup ()
  {
    return m_eTerminateGroup.getAsBooleanValue (DEFAULT_TERMINATE_GROUP);
  }

  @Nonnull
  public final EChange setTerminateGroup (final boolean bTerminateGroup)
  {
    return setTerminateGroup (ETriState.valueOf (bTerminateGroup));
  }

  @Nonnull
  public final EChange setTerminateGroup (@Nonnull final ETriState eTerminateGroup)
  {
    ValueEnforcer.notNull (eTerminateGroup, "TerminateGroup");
    if (eTerminateGroup.equals (m_eTerminateGroup))
      return EChange.UNCHANGED;
    m_eTerminateGroup = eTerminateGroup;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final PModeLegReliability rhs = (PModeLegReliability) o;
    return m_eAtLeastOnceContract.equals (rhs.m_eAtLeastOnceContract) &&
           m_eAtLeastOnceAckOnDelivery.equals (rhs.m_eAtLeastOnceAckOnDelivery) &&
           EqualsHelper.equals (m_sAtLeastOnceContractAcksTo, rhs.m_sAtLeastOnceContractAcksTo) &&
           m_eAtLeastOnceContractAckResponse.equals (rhs.m_eAtLeastOnceContractAckResponse) &&
           EqualsHelper.equals (m_sAtLeastOnceReplyPattern, rhs.m_sAtLeastOnceReplyPattern) &&
           m_eAtMostOnceContract.equals (rhs.m_eAtMostOnceContract) &&
           m_eInOrderContract.equals (rhs.m_eInOrderContract) &&
           m_eStartGroup.equals (rhs.m_eStartGroup) &&
           m_aCorrelation.equals (rhs.m_aCorrelation) &&
           m_eTerminateGroup.equals (rhs.m_eTerminateGroup);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eAtLeastOnceContract)
                                       .append (m_eAtLeastOnceAckOnDelivery)
                                       .append (m_sAtLeastOnceContractAcksTo)
                                       .append (m_eAtLeastOnceContractAckResponse)
                                       .append (m_sAtLeastOnceReplyPattern)
                                       .append (m_eAtMostOnceContract)
                                       .append (m_eInOrderContract)
                                       .append (m_eStartGroup)
                                       .append (m_aCorrelation)
                                       .append (m_eTerminateGroup)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("AtLeastOnceContract", m_eAtLeastOnceContract)
                                       .append ("AtLeastOnceAckOnDelivery", m_eAtLeastOnceAckOnDelivery)
                                       .append ("AtLeastOnceContractAcksTo", m_sAtLeastOnceContractAcksTo)
                                       .append ("AtLeastOnceContractAckResponse", m_eAtLeastOnceContractAckResponse)
                                       .append ("AtLeastOnceReplyPattern", m_sAtLeastOnceReplyPattern)
                                       .append ("AtMostOnceContract", m_eAtMostOnceContract)
                                       .append ("InOrderContract", m_eInOrderContract)
                                       .append ("StartGroup", m_eStartGroup)
                                       .append ("Correlation", m_aCorrelation)
                                       .append ("TerminateGroup", m_eTerminateGroup)
                                       .getToString ();
  }
}
