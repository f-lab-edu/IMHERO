package com.imhero.show.service;

import com.imhero.config.exception.ErrorCode;
import com.imhero.config.exception.ImheroApplicationException;
import com.imhero.show.domain.Show;
import com.imhero.show.dto.request.ShowRequest;
import com.imhero.show.dto.response.ShowResponse;
import com.imhero.show.repository.ShowRepository;
import com.imhero.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<ShowResponse> findAll(Pageable pageable, String delYn) {
        return showRepository.findAllByDelYn(pageable, delYn).map(ShowResponse::from);
    }

    public Long save(ShowRequest showRequest) {
        return showRepository.save(
                        Show.of(showRequest.getTitle(),
                                showRequest.getArtist(),
                                showRequest.getPlace(),
                                userService.getUserByIdOrElseThrow(showRequest.getUserId()),
                                showRequest.getShowFromDate(),
                                showRequest.getShowToDate(),
                                "N")).getId();
    }

    public void modify(ShowRequest showRequest) {
        getShowByIdOrElseThrow(showRequest.getId())
                .modify(showRequest.getTitle(),
                        showRequest.getArtist(),
                        showRequest.getPlace(),
                        userService.getUserByIdOrElseThrow(showRequest.getUserId()),
                        showRequest.getShowFromDate(),
                        showRequest.getShowToDate());
    }

    public void delete(Long showId) {
        getShowByIdOrElseThrow(showId).cancel();
    }

    @Transactional(readOnly = true)
    public Show getShowByIdOrElseThrow(Long showId) {
        return showRepository
                .findById(showId)
                .orElseThrow(() -> new ImheroApplicationException(ErrorCode.SHOW_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Show getShowByIdAndDelYn(Long id, String delYn) {
        return showRepository
                .findShowByIdAndDelYn(id, delYn)
                .orElseThrow(() -> new ImheroApplicationException(ErrorCode.SHOW_NOT_FOUND));
    }
}
