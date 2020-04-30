/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.multi.factor.authentication.timebased.otp.service.persistence.impl;

import com.liferay.multi.factor.authentication.timebased.otp.exception.NoSuchEntryException;
import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimebasedOTPEntry;
import com.liferay.multi.factor.authentication.timebased.otp.model.impl.MFATimebasedOTPEntryImpl;
import com.liferay.multi.factor.authentication.timebased.otp.model.impl.MFATimebasedOTPEntryModelImpl;
import com.liferay.multi.factor.authentication.timebased.otp.service.persistence.MFATimebasedOTPEntryPersistence;
import com.liferay.multi.factor.authentication.timebased.otp.service.persistence.impl.constants.MFATimebasedOTPPersistenceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the mfa timebased otp entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @generated
 */
@Component(service = MFATimebasedOTPEntryPersistence.class)
public class MFATimebasedOTPEntryPersistenceImpl
	extends BasePersistenceImpl<MFATimebasedOTPEntry>
	implements MFATimebasedOTPEntryPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>MFATimebasedOTPEntryUtil</code> to access the mfa timebased otp entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		MFATimebasedOTPEntryImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathFetchByUserId;
	private FinderPath _finderPathCountByUserId;

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param userId the user ID
	 * @return the matching mfa timebased otp entry
	 * @throws NoSuchEntryException if a matching mfa timebased otp entry could not be found
	 */
	@Override
	public MFATimebasedOTPEntry findByUserId(long userId)
		throws NoSuchEntryException {

		MFATimebasedOTPEntry mfaTimebasedOTPEntry = fetchByUserId(userId);

		if (mfaTimebasedOTPEntry == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("userId=");
			sb.append(userId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchEntryException(sb.toString());
		}

		return mfaTimebasedOTPEntry;
	}

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @return the matching mfa timebased otp entry, or <code>null</code> if a matching mfa timebased otp entry could not be found
	 */
	@Override
	public MFATimebasedOTPEntry fetchByUserId(long userId) {
		return fetchByUserId(userId, true);
	}

	/**
	 * Returns the mfa timebased otp entry where userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching mfa timebased otp entry, or <code>null</code> if a matching mfa timebased otp entry could not be found
	 */
	@Override
	public MFATimebasedOTPEntry fetchByUserId(
		long userId, boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {userId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByUserId, finderArgs, this);
		}

		if (result instanceof MFATimebasedOTPEntry) {
			MFATimebasedOTPEntry mfaTimebasedOTPEntry =
				(MFATimebasedOTPEntry)result;

			if (userId != mfaTimebasedOTPEntry.getUserId()) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_SELECT_MFATIMEBASEDOTPENTRY_WHERE);

			sb.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(userId);

				List<MFATimebasedOTPEntry> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByUserId, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {userId};
							}

							_log.warn(
								"MFATimebasedOTPEntryPersistenceImpl.fetchByUserId(long, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					MFATimebasedOTPEntry mfaTimebasedOTPEntry = list.get(0);

					result = mfaTimebasedOTPEntry;

					cacheResult(mfaTimebasedOTPEntry);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(
						_finderPathFetchByUserId, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (MFATimebasedOTPEntry)result;
		}
	}

	/**
	 * Removes the mfa timebased otp entry where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @return the mfa timebased otp entry that was removed
	 */
	@Override
	public MFATimebasedOTPEntry removeByUserId(long userId)
		throws NoSuchEntryException {

		MFATimebasedOTPEntry mfaTimebasedOTPEntry = findByUserId(userId);

		return remove(mfaTimebasedOTPEntry);
	}

	/**
	 * Returns the number of mfa timebased otp entries where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching mfa timebased otp entries
	 */
	@Override
	public int countByUserId(long userId) {
		FinderPath finderPath = _finderPathCountByUserId;

		Object[] finderArgs = new Object[] {userId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_MFATIMEBASEDOTPENTRY_WHERE);

			sb.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(userId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_USERID_USERID_2 =
		"mfaTimebasedOTPEntry.userId = ?";

	public MFATimebasedOTPEntryPersistenceImpl() {
		setModelClass(MFATimebasedOTPEntry.class);

		setModelImplClass(MFATimebasedOTPEntryImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the mfa timebased otp entry in the entity cache if it is enabled.
	 *
	 * @param mfaTimebasedOTPEntry the mfa timebased otp entry
	 */
	@Override
	public void cacheResult(MFATimebasedOTPEntry mfaTimebasedOTPEntry) {
		entityCache.putResult(
			entityCacheEnabled, MFATimebasedOTPEntryImpl.class,
			mfaTimebasedOTPEntry.getPrimaryKey(), mfaTimebasedOTPEntry);

		finderCache.putResult(
			_finderPathFetchByUserId,
			new Object[] {mfaTimebasedOTPEntry.getUserId()},
			mfaTimebasedOTPEntry);

		mfaTimebasedOTPEntry.resetOriginalValues();
	}

	/**
	 * Caches the mfa timebased otp entries in the entity cache if it is enabled.
	 *
	 * @param mfaTimebasedOTPEntries the mfa timebased otp entries
	 */
	@Override
	public void cacheResult(List<MFATimebasedOTPEntry> mfaTimebasedOTPEntries) {
		for (MFATimebasedOTPEntry mfaTimebasedOTPEntry :
				mfaTimebasedOTPEntries) {

			if (entityCache.getResult(
					entityCacheEnabled, MFATimebasedOTPEntryImpl.class,
					mfaTimebasedOTPEntry.getPrimaryKey()) == null) {

				cacheResult(mfaTimebasedOTPEntry);
			}
			else {
				mfaTimebasedOTPEntry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all mfa timebased otp entries.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(MFATimebasedOTPEntryImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the mfa timebased otp entry.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(MFATimebasedOTPEntry mfaTimebasedOTPEntry) {
		entityCache.removeResult(
			entityCacheEnabled, MFATimebasedOTPEntryImpl.class,
			mfaTimebasedOTPEntry.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(
			(MFATimebasedOTPEntryModelImpl)mfaTimebasedOTPEntry, true);
	}

	@Override
	public void clearCache(List<MFATimebasedOTPEntry> mfaTimebasedOTPEntries) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (MFATimebasedOTPEntry mfaTimebasedOTPEntry :
				mfaTimebasedOTPEntries) {

			entityCache.removeResult(
				entityCacheEnabled, MFATimebasedOTPEntryImpl.class,
				mfaTimebasedOTPEntry.getPrimaryKey());

			clearUniqueFindersCache(
				(MFATimebasedOTPEntryModelImpl)mfaTimebasedOTPEntry, true);
		}
	}

	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, MFATimebasedOTPEntryImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		MFATimebasedOTPEntryModelImpl mfaTimebasedOTPEntryModelImpl) {

		Object[] args = new Object[] {
			mfaTimebasedOTPEntryModelImpl.getUserId()
		};

		finderCache.putResult(
			_finderPathCountByUserId, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByUserId, args, mfaTimebasedOTPEntryModelImpl,
			false);
	}

	protected void clearUniqueFindersCache(
		MFATimebasedOTPEntryModelImpl mfaTimebasedOTPEntryModelImpl,
		boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {
				mfaTimebasedOTPEntryModelImpl.getUserId()
			};

			finderCache.removeResult(_finderPathCountByUserId, args);
			finderCache.removeResult(_finderPathFetchByUserId, args);
		}

		if ((mfaTimebasedOTPEntryModelImpl.getColumnBitmask() &
			 _finderPathFetchByUserId.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				mfaTimebasedOTPEntryModelImpl.getOriginalUserId()
			};

			finderCache.removeResult(_finderPathCountByUserId, args);
			finderCache.removeResult(_finderPathFetchByUserId, args);
		}
	}

	/**
	 * Creates a new mfa timebased otp entry with the primary key. Does not add the mfa timebased otp entry to the database.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key for the new mfa timebased otp entry
	 * @return the new mfa timebased otp entry
	 */
	@Override
	public MFATimebasedOTPEntry create(long mfaTimebasedOTPEntryId) {
		MFATimebasedOTPEntry mfaTimebasedOTPEntry =
			new MFATimebasedOTPEntryImpl();

		mfaTimebasedOTPEntry.setNew(true);
		mfaTimebasedOTPEntry.setPrimaryKey(mfaTimebasedOTPEntryId);

		mfaTimebasedOTPEntry.setCompanyId(CompanyThreadLocal.getCompanyId());

		return mfaTimebasedOTPEntry;
	}

	/**
	 * Removes the mfa timebased otp entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was removed
	 * @throws NoSuchEntryException if a mfa timebased otp entry with the primary key could not be found
	 */
	@Override
	public MFATimebasedOTPEntry remove(long mfaTimebasedOTPEntryId)
		throws NoSuchEntryException {

		return remove((Serializable)mfaTimebasedOTPEntryId);
	}

	/**
	 * Removes the mfa timebased otp entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry that was removed
	 * @throws NoSuchEntryException if a mfa timebased otp entry with the primary key could not be found
	 */
	@Override
	public MFATimebasedOTPEntry remove(Serializable primaryKey)
		throws NoSuchEntryException {

		Session session = null;

		try {
			session = openSession();

			MFATimebasedOTPEntry mfaTimebasedOTPEntry =
				(MFATimebasedOTPEntry)session.get(
					MFATimebasedOTPEntryImpl.class, primaryKey);

			if (mfaTimebasedOTPEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEntryException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(mfaTimebasedOTPEntry);
		}
		catch (NoSuchEntryException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected MFATimebasedOTPEntry removeImpl(
		MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(mfaTimebasedOTPEntry)) {
				mfaTimebasedOTPEntry = (MFATimebasedOTPEntry)session.get(
					MFATimebasedOTPEntryImpl.class,
					mfaTimebasedOTPEntry.getPrimaryKeyObj());
			}

			if (mfaTimebasedOTPEntry != null) {
				session.delete(mfaTimebasedOTPEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (mfaTimebasedOTPEntry != null) {
			clearCache(mfaTimebasedOTPEntry);
		}

		return mfaTimebasedOTPEntry;
	}

	@Override
	public MFATimebasedOTPEntry updateImpl(
		MFATimebasedOTPEntry mfaTimebasedOTPEntry) {

		boolean isNew = mfaTimebasedOTPEntry.isNew();

		if (!(mfaTimebasedOTPEntry instanceof MFATimebasedOTPEntryModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(mfaTimebasedOTPEntry.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					mfaTimebasedOTPEntry);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in mfaTimebasedOTPEntry proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom MFATimebasedOTPEntry implementation " +
					mfaTimebasedOTPEntry.getClass());
		}

		MFATimebasedOTPEntryModelImpl mfaTimebasedOTPEntryModelImpl =
			(MFATimebasedOTPEntryModelImpl)mfaTimebasedOTPEntry;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (mfaTimebasedOTPEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				mfaTimebasedOTPEntry.setCreateDate(now);
			}
			else {
				mfaTimebasedOTPEntry.setCreateDate(
					serviceContext.getCreateDate(now));
			}
		}

		if (!mfaTimebasedOTPEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				mfaTimebasedOTPEntry.setModifiedDate(now);
			}
			else {
				mfaTimebasedOTPEntry.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (mfaTimebasedOTPEntry.isNew()) {
				session.save(mfaTimebasedOTPEntry);

				mfaTimebasedOTPEntry.setNew(false);
			}
			else {
				mfaTimebasedOTPEntry = (MFATimebasedOTPEntry)session.merge(
					mfaTimebasedOTPEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!_columnBitmaskEnabled) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}

		entityCache.putResult(
			entityCacheEnabled, MFATimebasedOTPEntryImpl.class,
			mfaTimebasedOTPEntry.getPrimaryKey(), mfaTimebasedOTPEntry, false);

		clearUniqueFindersCache(mfaTimebasedOTPEntryModelImpl, false);
		cacheUniqueFindersCache(mfaTimebasedOTPEntryModelImpl);

		mfaTimebasedOTPEntry.resetOriginalValues();

		return mfaTimebasedOTPEntry;
	}

	/**
	 * Returns the mfa timebased otp entry with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry
	 * @throws NoSuchEntryException if a mfa timebased otp entry with the primary key could not be found
	 */
	@Override
	public MFATimebasedOTPEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEntryException {

		MFATimebasedOTPEntry mfaTimebasedOTPEntry = fetchByPrimaryKey(
			primaryKey);

		if (mfaTimebasedOTPEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEntryException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return mfaTimebasedOTPEntry;
	}

	/**
	 * Returns the mfa timebased otp entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry
	 * @throws NoSuchEntryException if a mfa timebased otp entry with the primary key could not be found
	 */
	@Override
	public MFATimebasedOTPEntry findByPrimaryKey(long mfaTimebasedOTPEntryId)
		throws NoSuchEntryException {

		return findByPrimaryKey((Serializable)mfaTimebasedOTPEntryId);
	}

	/**
	 * Returns the mfa timebased otp entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param mfaTimebasedOTPEntryId the primary key of the mfa timebased otp entry
	 * @return the mfa timebased otp entry, or <code>null</code> if a mfa timebased otp entry with the primary key could not be found
	 */
	@Override
	public MFATimebasedOTPEntry fetchByPrimaryKey(long mfaTimebasedOTPEntryId) {
		return fetchByPrimaryKey((Serializable)mfaTimebasedOTPEntryId);
	}

	/**
	 * Returns all the mfa timebased otp entries.
	 *
	 * @return the mfa timebased otp entries
	 */
	@Override
	public List<MFATimebasedOTPEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the mfa timebased otp entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa timebased otp entries
	 * @param end the upper bound of the range of mfa timebased otp entries (not inclusive)
	 * @return the range of mfa timebased otp entries
	 */
	@Override
	public List<MFATimebasedOTPEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the mfa timebased otp entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa timebased otp entries
	 * @param end the upper bound of the range of mfa timebased otp entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of mfa timebased otp entries
	 */
	@Override
	public List<MFATimebasedOTPEntry> findAll(
		int start, int end,
		OrderByComparator<MFATimebasedOTPEntry> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the mfa timebased otp entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATimebasedOTPEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa timebased otp entries
	 * @param end the upper bound of the range of mfa timebased otp entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of mfa timebased otp entries
	 */
	@Override
	public List<MFATimebasedOTPEntry> findAll(
		int start, int end,
		OrderByComparator<MFATimebasedOTPEntry> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<MFATimebasedOTPEntry> list = null;

		if (useFinderCache) {
			list = (List<MFATimebasedOTPEntry>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_MFATIMEBASEDOTPENTRY);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_MFATIMEBASEDOTPENTRY;

				sql = sql.concat(MFATimebasedOTPEntryModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<MFATimebasedOTPEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the mfa timebased otp entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (MFATimebasedOTPEntry mfaTimebasedOTPEntry : findAll()) {
			remove(mfaTimebasedOTPEntry);
		}
	}

	/**
	 * Returns the number of mfa timebased otp entries.
	 *
	 * @return the number of mfa timebased otp entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_MFATIMEBASEDOTPENTRY);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "mfaTimebasedOTPEntryId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_MFATIMEBASEDOTPENTRY;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return MFATimebasedOTPEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the mfa timebased otp entry persistence.
	 */
	@Activate
	public void activate() {
		MFATimebasedOTPEntryModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		MFATimebasedOTPEntryModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			MFATimebasedOTPEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			MFATimebasedOTPEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathFetchByUserId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			MFATimebasedOTPEntryImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByUserId", new String[] {Long.class.getName()},
			MFATimebasedOTPEntryModelImpl.USERID_COLUMN_BITMASK);

		_finderPathCountByUserId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] {Long.class.getName()});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(MFATimebasedOTPEntryImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = MFATimebasedOTPPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.multi.factor.authentication.timebased.otp.model.MFATimebasedOTPEntry"),
			true);
	}

	@Override
	@Reference(
		target = MFATimebasedOTPPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = MFATimebasedOTPPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_MFATIMEBASEDOTPENTRY =
		"SELECT mfaTimebasedOTPEntry FROM MFATimebasedOTPEntry mfaTimebasedOTPEntry";

	private static final String _SQL_SELECT_MFATIMEBASEDOTPENTRY_WHERE =
		"SELECT mfaTimebasedOTPEntry FROM MFATimebasedOTPEntry mfaTimebasedOTPEntry WHERE ";

	private static final String _SQL_COUNT_MFATIMEBASEDOTPENTRY =
		"SELECT COUNT(mfaTimebasedOTPEntry) FROM MFATimebasedOTPEntry mfaTimebasedOTPEntry";

	private static final String _SQL_COUNT_MFATIMEBASEDOTPENTRY_WHERE =
		"SELECT COUNT(mfaTimebasedOTPEntry) FROM MFATimebasedOTPEntry mfaTimebasedOTPEntry WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"mfaTimebasedOTPEntry.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No MFATimebasedOTPEntry exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No MFATimebasedOTPEntry exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		MFATimebasedOTPEntryPersistenceImpl.class);

	static {
		try {
			Class.forName(MFATimebasedOTPPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new ExceptionInInitializerError(classNotFoundException);
		}
	}

}