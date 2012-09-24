/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cubeia.games.poker.activator;

import java.util.List;

import com.cubeia.games.poker.entity.TableConfigTemplate;
import com.cubeia.games.poker.entity.TableConfigTemplateDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DatabaseTableConfigTemplateProvider implements TableConfigTemplateProvider {

	@Inject
	private TableConfigTemplateDao dao;
	
	@Override
	public List<TableConfigTemplate> getTemplates() {
		return dao.get();
	}
}
