/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.tweety.agents.gridworldsim.serverapi;

import net.sf.tweety.agents.gridworldsim.commons.ConnectionException;
import net.sf.tweety.agents.gridworldsim.serverapi.perceptions.GridWorldPerception;

/**
 * This item is used for the queue of incoming incidents for clients. It can wrap either a new {@link GridWorldPerception}
 * or a {@link ConnectionException} in case disconnection from the server happened.
 * @author Stefan Tittel <bugreports@tittel.net>
 */
public class ClientQueueItem {

    private final GridWorldPerception gwPercept;
    private final short type;
    public static final short PERCEPTION=0;
    public static final short DISCONNECT=1;
    public final ConnectionException ex;

    /**
     * Create a new {@code ClientQueueItem} for {@link GridWorldPerception}s.
     * @param gwPercept the {@link GridWorldPerception} this {@code ClientQueueItem} should contain
     */
    public ClientQueueItem(GridWorldPerception gwPercept) {
        this.gwPercept=gwPercept;
        type=PERCEPTION;
        ex=null;
    }

    /**
     * Create a new {@code ClientQueueItem} for {@link ConnectionException}s.
     * @param ex the {@link ConnectionException} this {@code ClientQueueItem} should contain
     */
    public ClientQueueItem(ConnectionException ex) {
        this.ex=ex;
        gwPercept=null;
        type=DISCONNECT;
    }

    /**
     * Gets the {@link GridWorldPerception} contained in this {@code ClientQueueItem} (if applicable)
     * @return the {@link GridWorldPerception} contained in this {@code ClientQueueItem} (or {@code null})
     */
    public GridWorldPerception getGwPercept() {
        return gwPercept;
    }

    /**
     * Gets the type of this {@code ClientQueueItem}.
     * @return the type of this {@code ClientQueueItem}
     */
    public short getType() {
        return type;
    }

    /**
     * Get the {@link ConnectionException} contained in this {@code ClientQueueItem} (if applicable).
     * @return the {@link ConnectionException} contained in this {@code ClientQueueItem} (or {@code null})
     */
    public ConnectionException getConnectionException() {
        return ex;
    }
}
