/*
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.elucidator.maven.analyzer.indexer;

import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.observers.AbstractTransferListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging while transferring data
 */
public class TransferListener extends AbstractTransferListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferListener.class);

    private long timestamp;
    private long transfer;
    private long progress_block;
    private long lastSizeReport = 0;
    private static final long SIZE_1M = 1024 * 1024;

    @Override
    public void transferStarted( TransferEvent transferEvent )
    {
        LOGGER.info("Downloading " + transferEvent.getResource().getName());
        timestamp = transferEvent.getTimestamp();
        long totalSize;
        this.transfer = 0;
        this.progress_block = 0;
        LOGGER.info("transferStarted");
        if ((transferEvent.getEventType() == TransferEvent.TRANSFER_STARTED) /* 1 */
                &&
                (transferEvent.getRequestType() == TransferEvent.REQUEST_GET) /* 5 */) {
            final String message = "Downloading: " + transferEvent.getResource().getName() + " to "
                    + transferEvent.getLocalFile().toString();
            LOGGER.info(message);
            totalSize = transferEvent.getResource().getContentLength();
            if (totalSize != -1) {
                LOGGER.info("Start download of " + totalSize + " bytes");
            }
        }
    }

    @Override
    public void transferProgress( TransferEvent transferEvent, byte[] buffer, int length )
    {
        transfer += length;
        progress_block += length;
        //Report each Mb
        if ((transfer / SIZE_1M) != this.lastSizeReport) {
            LOGGER.info("Transferred " + transfer / SIZE_1M + "Mb (" + transfer + " bytes)");
            this.lastSizeReport = transfer / SIZE_1M;
        }
    }

    @Override
    public void transferCompleted( TransferEvent transferEvent) {
        final double duration = (double) (transferEvent.getTimestamp() - timestamp) / 1000;

        final String message = "Transfer finished. " + transfer / SIZE_1M + "Mb (" + transfer + " bytes) copied in " + duration + " seconds";

        LOGGER.info(message);
    }

    @Override
    public void transferError( TransferEvent transferEvent) {
        LOGGER.info("Transfer error: " + transferEvent.getException());
    }


    @Override
    public void debug(String message) {
        LOGGER.debug("Message = " + message);
    }

}
