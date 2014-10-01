/*
 *
 *   Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 * /
 */

package org.wso2.andes.server.slot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.kernel.AndesException;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.andes.kernel.MessageStore;
import org.wso2.andes.kernel.MessagingEngine;

import java.util.List;

/**
 * This class contains methods used by both coordinator and member nodes
 */
public class SlotUtils {

    private static Log log = LogFactory.getLog(SlotUtils.class);


    /**
     * @param slot
     * @return whether the slot is empty or not
     */
    public static boolean checkSlotEmptyFromMessageStore(Slot slot) {
        MessageStore messageStore = MessagingEngine.getInstance().getDurableMessageStore();
        try {
            List<AndesMessageMetadata> messagesReturnedFromCassandra =
                    messageStore.getMetaDataList(slot.getQueueName(), slot.getStartMessageId(),
                            slot.getEndMessageId());
            if (messagesReturnedFromCassandra == null || messagesReturnedFromCassandra.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (AndesException e) {
            log.error("Error occurred while querying metadata from cassandra", e);
            return false;
        }
    }
}
