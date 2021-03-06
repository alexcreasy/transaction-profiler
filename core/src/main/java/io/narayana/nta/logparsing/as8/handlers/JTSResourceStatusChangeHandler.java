/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package io.narayana.nta.logparsing.as8.handlers;

import io.narayana.nta.logparsing.common.AbstractHandler;

import java.util.regex.Matcher;

/**
 * @Author Alex Creasy &lt;a.r.creasy@newcastle.ac.uk$gt;
 * Date: 08/08/2013
 * Time: 00:28
 */
public class JTSResourceStatusChangeHandler extends JbossAS8AbstractHandler {

    private static final String REGEX = "BasicAction::do(?<METHOD>Prepare|Commit|Abort)\\(\\)\\sresult\\sfor\\saction-id\\s\\("
            + AbstractHandler.PATTERN_TXUID + "\\)\\son\\srecord\\sid:\\s\\(" + PATTERN_RMUID + "\\)\\sis\\s\\(TwoPhaseOutcome\\.(?<OUTCOME>[A-Z_]+)\\)"
            + "\\snode\\sid:\\s\\((?<NODE>[^\\)]+)\\)";


    public JTSResourceStatusChangeHandler() {

        super(REGEX);
    }

    @Override
    public void handle(Matcher matcher, String line) {

        String rmuid = matcher.group(RMUID);
        if (rmuid.equals(LAST_RESOURCE_ID)) {
            rmuid = matcher.group(TXUID) + ":" + LAST_RESOURCE_ID;
        }
        service.resourceStatusOutcomeJTS(rmuid, matcher.group("OUTCOME"),
                parseTimestamp(matcher.group(TIMESTAMP)));
    }
}
