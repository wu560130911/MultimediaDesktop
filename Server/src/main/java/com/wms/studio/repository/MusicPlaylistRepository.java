/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wms.studio.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wms.studio.entity.Music;
import com.wms.studio.entity.MusicPlaylist;
import com.wms.studio.entity.User;

/**
 * @author WMS
 * 
 */
public interface MusicPlaylistRepository extends
		JpaRepository<MusicPlaylist, Long>,
		JpaSpecificationExecutor<MusicPlaylist> {

	List<MusicPlaylist> findByUser(User user);

	Page<MusicPlaylist> findByUser(User user, Pageable pageable);

	List<MusicPlaylist> findByUserAndMusic(User user, Music music);
}
