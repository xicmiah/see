/*
 * Copyright 2011 Vasily Shiyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package see.exceptions;

import org.parboiled.errors.ErrorUtils;
import org.parboiled.support.ParsingResult;

/**
 * Parboiled-specific pretty parsing exception
 */
public class ParboiledParseException extends RuntimeException {
    private final ParsingResult<?> parsingResult;

    /**
     * Construct instance from parse result
     * @param result parse result
     */
    public ParboiledParseException(ParsingResult<?> result) {
        super(ErrorUtils.printParseErrors(result));
        this.parsingResult = result;
    }

    public ParsingResult<?> getParsingResult() {
        return parsingResult;
    }
}
