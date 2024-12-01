package com.etna.primeflixplus.services;

import com.etna.primeflixplus.dtos.ProfileCreationDto;
import com.etna.primeflixplus.dtos.ProfileModificationDto;
import com.etna.primeflixplus.entities.Profile;
import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.entities.VideoProfile;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.exception.CustomMessageException;
import com.etna.primeflixplus.repositories.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile addBasicProfile(User user, ProfileCreationDto profileCreationDto) throws CustomGlobalException {
        try {
            Profile profile = new Profile();
            profile.setPseudo(profileCreationDto.getPseudo());
            profile.setImage(profileCreationDto.getImage());
            profile.setUser(user);
            if (profileCreationDto.getIsYoung() != null)
                profile.setIsYoung(profileCreationDto.getIsYoung());
            profileRepository.save(profile);
            return profile;
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.CONFLICT, CustomMessageException.PROFILE_ADD_FAILED);
        }
    }

    public Profile addMainProfile(User user, ProfileCreationDto profileCreationDto) throws CustomGlobalException {
        try {
            List<Profile> profiles = profileRepository.getAllByUser(user);
            if (!profiles.isEmpty())
                for (Profile profile : profiles)
                    if (profile.getIsMainProfile() == Boolean.TRUE)
                        throw new CustomGlobalException(HttpStatus.CONFLICT, CustomMessageException.PROFILE_ADD_MAIN_FAILED);
            Profile profile = new Profile();
            profile.setPseudo(profileCreationDto.getPseudo());
            profile.setImage(profileCreationDto.getImage());
            profile.setUser(user);
            profile.setIsMainProfile(Boolean.TRUE);
            profileRepository.save(profile);
            return profile;
        } catch (CustomGlobalException e) {
            throw new CustomGlobalException(HttpStatus.CONFLICT, CustomMessageException.PROFILE_ADD_MAIN_FAILED);
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.CONFLICT, CustomMessageException.PROFILE_ADD_FAILED);
        }
    }

    public List<Profile> getProfiles(User user) throws CustomGlobalException {
        try {
            List<Profile> profiles = profileRepository.getAllByUser(user);
            if (profiles.isEmpty())
                throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.PROFILE_GET_NOTHING);
            return profiles;
        } catch (CustomGlobalException e) {
            throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.PROFILE_GET_NOTHING);
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.PROFILE_GET_FAILED);
        }
    }

    public Profile updateProfile(User user, ProfileModificationDto profileModificationDto) throws CustomGlobalException {
        Profile profile = getProfileById(profileModificationDto.getId());
        if (!Objects.equals(profile.getUser().getId(), user.getId()))
            throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.PROFILE_GET_ONE_FAILED);
        if (profileModificationDto.getPseudo() != null)
            profile.setPseudo(profileModificationDto.getPseudo());
        if (profileModificationDto.getImage() != null)
            profile.setImage(profileModificationDto.getImage());
        if (profileModificationDto.getReceiveNewFilms() != null)
            profile.setReceiveNewFilms(profileModificationDto.getReceiveNewFilms());
        if (profileModificationDto.getReceiveNewSeasons() != null)
            profile.setReceiveNewSeasons(profileModificationDto.getReceiveNewSeasons());
        if (profileModificationDto.getReceiveNewSeries() != null)
            profile.setReceiveNewSeries(profileModificationDto.getReceiveNewSeries());
        if (profileModificationDto.getReceiveNewsletter() != null)
            profile.setReceiveNewsletter(profileModificationDto.getReceiveNewsletter());
        profile.setUpdatedDate(LocalDateTime.now());
        profileRepository.save(profile);
        return profile;
    }

    public Profile getProfileById(Integer idProfile) throws CustomGlobalException {
        Optional<Profile> profile = profileRepository.findById(idProfile);
        if (profile.isEmpty())
            throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.PROFILE_GET_ONE_FAILED);
        return profile.get();
    }

    public void deleteProfile(Integer id) throws CustomGlobalException {
        Profile profile = getProfileById(id);
        profileRepository.delete(profile);
    }
}
