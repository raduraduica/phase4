/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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
package com.helger.as4lib.model.pmode;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.state.ETriState;

/**
 * Security part.
 *
 * @author Philip Helger
 */
public class PModeLegSecurity
{
  // TODO SET DEFAULT VALUES
  public static final boolean DEFAULT_USERNAME_TOKEN_DIGEST = false;
  public static final boolean DEFAULT_USERNAME_TOKEN_NONCE = false;
  public static final boolean DEFAULT_USERNAME_TOKEN_CREATED = false;
  public static final boolean DEFAULT_PMODE_AUTHORIZE = false;
  public static final boolean DEFAULT_SEND_RECEIPT = false;

  /**
   * This parameter has two possible values, 1.0 and 1.1. The value of this
   * parameter represents the version of WS-Security to be used.
   */
  private String m_sWSSVersion;
  /**
   * The value of this parameter is a list of the names of XML elements (inside
   * the SOAP envelope) that should be signed, as well as whether or not
   * attachments should also be signed. The list is represented in two sublists
   * that extend this parameter: Sign.Element[] and Sign.Attachment[]. An
   * element within the Element[] list could be specified either by its XML name
   * or by its qualified name (its XML name and the namespace to which it
   * belongs). An element within the Attachment[] list is identified by the
   * Content-Id.
   */
  private ICommonsList <String> m_aX509Sign;
  /**
   * The value of this parameter identifies the public certificate to use when
   * verifying signed data.
   */
  private String m_sX509SignatureCertificate;
  /**
   * The value of this parameter identifies the algorithm that is used to
   * compute the digest of the message being signed. The definitions for these
   * values are in the [XMLDSIG] specification.
   */
  private String m_sX509SignatureHashFunction;
  /**
   * The value of this parameter identifies the algorithm that is used to
   * compute the value of the digital signature. The definitions for these
   * values are found in the [XMLDSIG] or [XMLENC] specifications.
   */
  private String m_sX509SignatureAlgorithm;
  /**
   * The value of this parameter lists the names of XML elements(inside the SOAP
   * envelope) that should be encrypted, as well as whether or not attachments
   * should also be encrypted. The list is represented in two sublists that
   * extend this parameter: Encrypt.Element[] and Encrypt.Attachment[]. An
   * element within these lists is identified as in Security.X509.Sign lists.
   */
  private ICommonsList <String> m_aX509EncryptionEncrypt;
  /**
   * The value of this parameter identifies the public certificate to use when
   * encrypting data.
   */
  private String m_sX509EncryptionCertificate;
  /**
   * The value of this parameter identifies the encryption algorithm to be used.
   * The definitions for these values are found in the [XMLENC] specification.
   */
  private String m_sX509EncryptionAlgorithm;
  /**
   * The integer value of this parameter describes the effective strength the
   * encryption algorithm MUST provide in terms of "effective" or random bits.
   * The value is less than the key length in bits when check bits are used in
   * the key. So, for example the 8 check bits of a 64-bit DES key would not be
   * included in the count, and to require a minimum strength the same as
   * supplied by DES would be reported by setting MinimumStrength to 56.
   */
  private Integer m_aX509EncryptionMinimumStrength;
  /**
   * The value of this parameter is the username to include in a WSS Username
   * Token.
   */
  private String m_sUsernameTokenUsername;
  /**
   * The value of this parameter is the password to use inside a WSS Username
   * Token.
   */
  private String m_sUsernameTokenPassword;
  /**
   * The Boolean value of this parameter indicates whether a password digest
   * should be included in the WSS UsernameToken element.
   */
  private ETriState m_eUsernameTokenDigest = ETriState.UNDEFINED;
  /**
   * The Boolean value of this parameter indicates whether the WSS UsernameToken
   * element should contain a Nonce element.
   */
  private ETriState m_eUsernameTokenNonce = ETriState.UNDEFINED;
  /**
   * The Boolean value of this parameter indicates whether the WSS UsernameToken
   * element should have a Created timestamp element.
   */
  private ETriState m_eUsernameTokenCreated = ETriState.UNDEFINED;
  /**
   * The Boolean value of this parameter indicates whether messages on this MEP
   * leg must be authorized for processing under this P-Mode. If the parameter
   * is "true" this implies that either
   * PMode.Responder.Authorization.{username/password}, if the message is sent
   * by Responder, or PMode.Initiator.Authorization if the message is sent by
   * Initiator, must be used for this purpose, as specified in Section 7.10. For
   * example, when set to "true" for a PullRequest message sent by the
   * Initiator, the pulling will only be authorized over the MPC indicated by
   * this Pull signal if (a) the MPC is the same as specified in the P-Mode leg
   * for the pulled message , and (b) the signal contains the right credentials
   * (e.g. username/password).
   */
  private ETriState m_ePModeAuthorize = ETriState.UNDEFINED;
  /**
   * The Boolean value of this parameter indicates whether a signed receipt
   * (Receipt ebMS signal) containing a digest of the message must be sent back.
   */
  private ETriState m_eSendReceipt = ETriState.UNDEFINED;
  /**
   * This parameter indicates whether the Receipt signal is to be sent as a
   * callback (value "callback"), or synchronously in the back-channel response
   * (value "response"). If not present, any pattern may be used.
   */
  private String m_sSendReceiptReplyPattern;

  public String getWSSVersion ()
  {
    return m_sWSSVersion;
  }

  public void setWSSVersion (final String sWSSVersion)
  {
    m_sWSSVersion = sWSSVersion;
  }

  public ICommonsList <String> getX509Sign ()
  {
    return m_aX509Sign;
  }

  public void setX509Sign (final ICommonsList <String> aX509Sign)
  {
    m_aX509Sign = aX509Sign;
  }

  public String getX509SignatureCertificate ()
  {
    return m_sX509SignatureCertificate;
  }

  public void setX509SignatureCertificate (final String sX509SignatureCertificate)
  {
    m_sX509SignatureCertificate = sX509SignatureCertificate;
  }

  public String getX509SignatureHashFunction ()
  {
    return m_sX509SignatureHashFunction;
  }

  public void setX509SignatureHashFunction (final String sX509SignatureHashFunction)
  {
    m_sX509SignatureHashFunction = sX509SignatureHashFunction;
  }

  public String getX509SignatureAlgorithm ()
  {
    return m_sX509SignatureAlgorithm;
  }

  public void setX509SignatureAlgorithm (final String sX509SignatureAlgorithm)
  {
    m_sX509SignatureAlgorithm = sX509SignatureAlgorithm;
  }

  public ICommonsList <String> getX509EncryptionEncrypt ()
  {
    return m_aX509EncryptionEncrypt;
  }

  public void setX509EncryptionEncrypt (final ICommonsList <String> aX509EncryptionEncrypt)
  {
    m_aX509EncryptionEncrypt = aX509EncryptionEncrypt;
  }

  public String getX509EncryptionCertificate ()
  {
    return m_sX509EncryptionCertificate;
  }

  public void setX509EncryptionCertificate (final String sX509EncryptionCertificate)
  {
    m_sX509EncryptionCertificate = sX509EncryptionCertificate;
  }

  public String getX509EncryptionAlgorithm ()
  {
    return m_sX509EncryptionAlgorithm;
  }

  public void setX509EncryptionAlgorithm (final String sX509EncryptionAlgorithm)
  {
    m_sX509EncryptionAlgorithm = sX509EncryptionAlgorithm;
  }

  public Integer getX509EncryptionMinimumStrength ()
  {
    return m_aX509EncryptionMinimumStrength;
  }

  public void setX509EncryptionMinimumStrength (final Integer aX509EncryptionMinimumStrength)
  {
    m_aX509EncryptionMinimumStrength = aX509EncryptionMinimumStrength;
  }

  public String getUsernameTokenUsername ()
  {
    return m_sUsernameTokenUsername;
  }

  public void setUsernameTokenUsername (final String sUsernameTokenUsername)
  {
    m_sUsernameTokenUsername = sUsernameTokenUsername;
  }

  public String getUsernameTokenPassword ()
  {
    return m_sUsernameTokenPassword;
  }

  public void setUsernameTokenPassword (final String sUsernameTokenPassword)
  {
    m_sUsernameTokenPassword = sUsernameTokenPassword;
  }

  public String getSendReceiptReplyPattern ()
  {
    return m_sSendReceiptReplyPattern;
  }

  public void setSendReceiptReplyPattern (final String sSendReceiptReplyPattern)
  {
    m_sSendReceiptReplyPattern = sSendReceiptReplyPattern;
  }

  public boolean getUsernameTokenDigest ()
  {
    return m_eUsernameTokenDigest.getAsBooleanValue (DEFAULT_USERNAME_TOKEN_DIGEST);
  }

  public void setUsernameTokenDigest (final boolean bUsernameTokenDigest)
  {
    setUsernameTokenDigest (ETriState.valueOf (bUsernameTokenDigest));
  }

  public void setUsernameTokenDigest (@Nonnull final ETriState eUsernameTokenDigest)
  {
    ValueEnforcer.notNull (eUsernameTokenDigest, "UsernameTokenDigest");
    m_eUsernameTokenDigest = eUsernameTokenDigest;
  }

  public boolean getUsernameTokenNonce ()
  {
    return m_eUsernameTokenNonce.getAsBooleanValue (DEFAULT_USERNAME_TOKEN_NONCE);
  }

  public void setUsernameTokenNonce (final boolean bUsernameTokenNonce)
  {
    setUsernameTokenNonce (ETriState.valueOf (bUsernameTokenNonce));
  }

  public void setUsernameTokenNonce (@Nonnull final ETriState eUsernameTokenNonce)
  {
    ValueEnforcer.notNull (eUsernameTokenNonce, "UsernameTokenNonce ");
    m_eUsernameTokenNonce = eUsernameTokenNonce;
  }

  public boolean getUsernameTokenCreated ()
  {
    return m_eUsernameTokenCreated.getAsBooleanValue (DEFAULT_USERNAME_TOKEN_CREATED);
  }

  public void setUsernameTokenCreated (final boolean bUsernameTokenCreated)
  {
    setUsernameTokenCreated (ETriState.valueOf (bUsernameTokenCreated));
  }

  public void setUsernameTokenCreated (@Nonnull final ETriState eUsernameTokenCreated)
  {
    ValueEnforcer.notNull (eUsernameTokenCreated, "UsernameTokenCreated ");
    m_eUsernameTokenCreated = eUsernameTokenCreated;
  }

  public boolean getPModeAuthorize ()
  {
    return m_ePModeAuthorize.getAsBooleanValue (DEFAULT_PMODE_AUTHORIZE);
  }

  public void setPModeAuthorize (final boolean bPModeAuthorize)
  {
    setPModeAuthorize (ETriState.valueOf (bPModeAuthorize));
  }

  public void setPModeAuthorize (@Nonnull final ETriState ePModeAuthorize)
  {
    ValueEnforcer.notNull (ePModeAuthorize, "PModeAuthorize ");
    m_ePModeAuthorize = ePModeAuthorize;
  }

  public boolean getSendReceipt ()
  {
    return m_eSendReceipt.getAsBooleanValue (DEFAULT_SEND_RECEIPT);
  }

  public void setSendReceipt (final boolean bSendReceipt)
  {
    setSendReceipt (ETriState.valueOf (bSendReceipt));
  }

  public void setSendReceipt (@Nonnull final ETriState eSendReceipt)
  {
    ValueEnforcer.notNull (eSendReceipt, "SendReceipt ");
    m_eSendReceipt = eSendReceipt;
  }
}
