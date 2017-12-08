package com.github.harrynp.tasty;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.FragmentStepDetailBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepDetailFragment extends Fragment implements Player.EventListener{
    FragmentStepDetailBinding mBinding;
    public static String STEP_EXTRA = "STEP_EXTRA";
    Step step;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private long currentPosition;
    private static final String LOG_TAG = StepDetailFragment.class.getSimpleName();


    public static StepDetailFragment newInstance(Step step){
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(STEP_EXTRA, Parcels.wrap(step));
        fragment.setArguments(args);
        return fragment;
    }
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        mPlayerView = mBinding.exoPlayer;
        if (getArguments() != null){
            step = Parcels.unwrap(getArguments().getParcelable(STEP_EXTRA));
        }
        if (step != null){
            if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()){
                mBinding.flStepDetailFragment.setVisibility(View.VISIBLE);
                mBinding.ivStepThumbnail.setVisibility(View.INVISIBLE);
                mBinding.exoPlayer.setVisibility(View.VISIBLE);
                initializeMediaSession();
                initializePlayer(Uri.parse(step.getVideoURL()));
            } else if (step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty()){
                mBinding.flStepDetailFragment.setVisibility(View.VISIBLE);
                mBinding.exoPlayer.setVisibility(View.INVISIBLE);
                mBinding.ivStepThumbnail.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(step.getThumbnailURL())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_food)
                                .fallback(R.drawable.placeholder_food)
                                .centerCrop())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mBinding.ivStepThumbnail);
            } else {
                mBinding.flStepDetailFragment.setVisibility(View.VISIBLE);
                mBinding.exoPlayer.setVisibility(View.INVISIBLE);
                mBinding.ivStepThumbnail.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(R.drawable.placeholder_food)
                        .apply(new RequestOptions()
                                .centerCrop())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mBinding.ivStepThumbnail);
            }
            mBinding.tvStepDetails.setText(step.getDescription());
        }

        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (step != null) {
            if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
                currentPosition = mExoPlayer.getCurrentPosition();
                releasePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (step != null) {
            if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
                initializeMediaSession();
                initializePlayer(Uri.parse(step.getVideoURL()));
            }
        }
    }

    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), LOG_TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MediaSessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri videoUri){
        if (mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mExoPlayer.addListener(this);
            mExoPlayer.seekTo(currentPosition);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "Tasty");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
            mBinding.pbBufferingIndicator.setVisibility(View.GONE);
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
            mBinding.pbBufferingIndicator.setVisibility(View.GONE);
        } else if (playbackState == Player.STATE_BUFFERING) {
            mBinding.pbBufferingIndicator.setVisibility(View.VISIBLE);
        } else {
            mBinding.pbBufferingIndicator.setVisibility(View.GONE);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }
}
