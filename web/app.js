// =========================================================
//  SocialSim – JavaScript port of the Java social network
// =========================================================

// ── Palette for avatars ──────────────────────────────────
const COLORS = ['#6c63ff', '#ff6584', '#26a69a', '#f9a825', '#ef5350', '#42a5f5'];
let colorIndex = 0;
function nextColor() { return COLORS[colorIndex++ % COLORS.length]; }

// ── Helpers ──────────────────────────────────────────────
function formatDate(date) {
  const d = date.getDate().toString().padStart(2, '0');
  const m = (date.getMonth() + 1).toString().padStart(2, '0');
  const y = date.getFullYear();
  const h = date.getHours().toString().padStart(2, '0');
  const min = date.getMinutes().toString().padStart(2, '0');
  return `${d}.${m}.${y} ${h}:${min}`;
}
function timeAgo(date) {
  const diff = Math.floor((Date.now() - date) / 1000);
  if (diff < 60) return 'just now';
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
  if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
  return `${Math.floor(diff / 86400)}d ago`;
}

// ── Exceptions ───────────────────────────────────────────
class ApplicationException extends Error {}
class EmptyContentException extends ApplicationException {
  constructor() { super('Post content cannot be empty.'); }
}
class AlreadyLikedException extends ApplicationException {
  constructor() { super('You already liked this post.'); }
}
class UserNotFoundException extends ApplicationException {
  constructor(name) { super(`User ${name} not found.`); }
}
class InvalidOptionException extends ApplicationException {
  constructor() { super('A poll must have at least 2 options.'); }
}

// ── User ─────────────────────────────────────────────────
class User {
  static #counter = 1;
  constructor(username, bio) {
    this.id = User.#counter++;
    this.username = username;
    this.bio = bio;
    this.joinDate = new Date();
    this.posts = [];
    this.notifications = [];
    this.color = nextColor();
  }
  get unreadCount() {
    return this.notifications.filter(n => !n.read).length;
  }
  toString() {
    return `User #${this.id} | @${this.username} | Bio: ${this.bio} | Joined: ${formatDate(this.joinDate)}`;
  }
}

// ── Post ─────────────────────────────────────────────────
class Post {
  static #counter = 1;
  constructor(author, content) {
    if (!content || content.trim() === '') throw new EmptyContentException();
    this.id = Post.#counter++;
    this.author = author;
    this.content = content;
    this.timestamp = new Date();
    this.likedBy = [];
    this.comments = [];
    this.type = 'text';
  }
  get likesCount() { return this.likedBy.length; }
  addLike(user) {
    if (this.likedBy.includes(user)) throw new AlreadyLikedException();
    this.likedBy.push(user);
  }
  removeLike(user) {
    const i = this.likedBy.indexOf(user);
    if (i !== -1) this.likedBy.splice(i, 1);
  }
  hasLiked(user) { return this.likedBy.includes(user); }
  addComment(comment) { this.comments.push(comment); comment.id = this.comments.length; }
}

// ── EventPost ────────────────────────────────────────────
class EventPost extends Post {
  constructor(author, content, eventDateTime, location) {
    super(author, content);
    this.eventDateTime = eventDateTime;
    this.location = location;
    this.type = 'event';
  }
}

// ── PollPost ─────────────────────────────────────────────
class PollPost extends Post {
  constructor(author, content, options) {
    super(author, content);
    const valid = options.filter(o => o.trim() !== '');
    if (valid.length < 2) throw new InvalidOptionException();
    this.options = valid;
    this.votes = {}; // username → option index
    this.type = 'poll';
  }
  vote(user, optionIndex) {
    this.votes[user.username] = optionIndex;
  }
  getVoteCount(optionIndex) {
    return Object.values(this.votes).filter(v => v === optionIndex).length;
  }
}

// ── Comment ──────────────────────────────────────────────
class Comment {
  static #counter = 1;
  constructor(author, post, content) {
    this.id = Comment.#counter++;
    this.author = author;
    this.post = post;
    this.content = content;
    this.timestamp = new Date();
  }
}

// ── Notification ─────────────────────────────────────────
class Notification {
  static #counter = 1;
  constructor(sender, recipient, content) {
    this.id = Notification.#counter++;
    this.sender = sender;
    this.recipient = recipient;
    this.content = content;
    this.timestamp = new Date();
    this.read = false;
  }
}

// ── SocialNetwork ────────────────────────────────────────
class SocialNetwork {
  constructor(name) {
    this.name = name;
    this.users = [];
    this.posts = [];
  }
  findUserByUsername(username) {
    const u = this.users.find(u => u.username === username);
    if (!u) throw new UserNotFoundException(username);
    return u;
  }
  addUser(user) {
    try { this.findUserByUsername(user.username); return false; }
    catch { this.users.push(user); return true; }
  }
  createPost(author, content) {
    const post = new Post(author, content);
    this.posts.unshift(post);
    author.posts.unshift(post);
    return post;
  }
  createEventPost(author, content, eventDateTime, location) {
    const post = new EventPost(author, content, eventDateTime, location);
    this.posts.unshift(post);
    author.posts.unshift(post);
    return post;
  }
  createPollPost(author, content, options) {
    const post = new PollPost(author, content, options);
    this.posts.unshift(post);
    author.posts.unshift(post);
    return post;
  }
  likePost(post, user) {
    post.addLike(user);
    if (user !== post.author) {
      const n = new Notification(user, post.author, `${user.username} liked your post`);
      post.author.notifications.unshift(n);
    }
  }
  unlikePost(post, user) {
    post.removeLike(user);
  }
  addComment(author, post, content) {
    const c = new Comment(author, post, content);
    post.addComment(c);
    if (author !== post.author) {
      const n = new Notification(author, post.author, `${author.username} commented on your post`);
      post.author.notifications.unshift(n);
    }
    return c;
  }
}

// =========================================================
//  BOOTSTRAP – same demo data as App.java
// =========================================================
const network = new SocialNetwork('SocialSim');

const nor = new User('Nor', 'Hello!');
const dan = new User('Dan', 'Hi there!');
const ron = new User('Ron', 'I love coding!');

network.addUser(nor);
network.addUser(dan);
network.addUser(ron);

let post1, post2, post3;
try {
  post1 = network.createPost(nor, 'Just joined SocialSim!');
  post2 = network.createEventPost(dan, 'Hello everyone!',
    new Date(2026, 5, 10, 18, 0), 'Tel Aviv');
  post3 = network.createPollPost(ron, 'Java is awesome?', ['Yes', 'No', 'Maybe']);

  network.addComment(dan, post1, 'Welcome Nor!');
  network.addComment(ron, post1, 'Great to have you here!');
  network.addComment(nor, post2, 'Nice to meet everyone!');

  network.likePost(post1, dan);
  network.likePost(post1, ron);
  network.likePost(post2, nor);
} catch (e) {
  console.error('Setup error:', e.message);
}

// =========================================================
//  UI STATE
// =========================================================
let activeUser = nor;
let currentPostType = 'text';

// =========================================================
//  RENDER HELPERS
// =========================================================
function avatar(user, size = 44) {
  return `<div class="user-avatar" style="width:${size}px;height:${size}px;background:${user.color};font-size:${Math.round(size * 0.4)}px">${user.username[0].toUpperCase()}</div>`;
}

// ── Render sidebar users ─────────────────────────────────
function renderUsers() {
  const list = document.getElementById('userList');
  list.innerHTML = network.users.map(u => `
    <div class="user-card ${u === activeUser ? 'active-card' : ''}" data-username="${u.username}">
      ${avatar(u, 42)}
      <div class="user-info">
        <div class="user-name">@${u.username}</div>
        <div class="user-bio">${u.bio}</div>
        <div class="user-stats">${u.posts.length} posts</div>
      </div>
    </div>
  `).join('');

  list.querySelectorAll('.user-card').forEach(card => {
    card.addEventListener('click', () => {
      activeUser = network.findUserByUsername(card.dataset.username);
      document.getElementById('activeUserSelect').value = activeUser.username;
      document.getElementById('activeUserLabel').textContent = activeUser.username;
      renderAll();
    });
  });
}

// ── Render single post ───────────────────────────────────
function renderPost(post) {
  const liked = post.hasLiked(activeUser);
  const typeClass = post.type + '-post';
  const badgeClass = 'badge-' + post.type;
  const badgeLabel = post.type === 'text' ? 'Post' : post.type === 'event' ? 'Event' : 'Poll';

  let extraHTML = '';

  if (post instanceof EventPost) {
    const evDate = post.eventDateTime instanceof Date
      ? formatDate(post.eventDateTime)
      : String(post.eventDateTime);
    extraHTML = `
      <div class="event-info">
        <span>&#128205; <strong>${post.location}</strong></span>
        <span>&#128197; <strong>${evDate}</strong></span>
      </div>`;
  }

  if (post instanceof PollPost) {
    extraHTML = `<div class="poll-options" id="poll-${post.id}">` +
      post.options.map((opt, i) => {
        const votedClass = post.votes[activeUser.username] === i ? 'voted' : '';
        return `<label class="poll-option ${votedClass}">
          <input type="radio" name="poll-${post.id}" value="${i}" ${post.votes[activeUser.username] === i ? 'checked' : ''} />
          ${opt}
        </label>`;
      }).join('') +
      `</div>`;
  }

  const commentsHTML = post.comments.map(c => `
    <div class="comment-item">
      <div class="comment-avatar" style="background:${c.author.color}">${c.author.username[0]}</div>
      <div class="comment-bubble">
        <div class="comment-author">@${c.author.username}</div>
        <div class="comment-text">${escHtml(c.content)}</div>
        <div class="comment-time">${timeAgo(c.timestamp)}</div>
      </div>
    </div>
  `).join('');

  return `
    <div class="post-card" id="post-card-${post.id}">
      <div class="post-type-bar ${typeClass}"></div>
      <div class="post-header">
        <div class="post-avatar" style="background:${post.author.color}">${post.author.username[0]}</div>
        <div class="post-meta">
          <div class="post-author">@${post.author.username}</div>
          <div class="post-time">${timeAgo(post.timestamp)}</div>
        </div>
        <span class="post-type-badge ${badgeClass}">${badgeLabel}</span>
      </div>
      <div class="post-body">
        <div class="post-content">${escHtml(post.content)}</div>
        ${extraHTML}
      </div>
      <div class="post-actions">
        <button class="action-btn like-btn ${liked ? 'liked' : ''}" data-post-id="${post.id}">
          ${liked ? '&#10084;&#65039;' : '&#129293;'} <span class="like-count">${post.likesCount} Like${post.likesCount !== 1 ? 's' : ''}</span>
        </button>
        <button class="action-btn comment-toggle-btn" data-post-id="${post.id}">
          &#128172; ${post.comments.length} Comment${post.comments.length !== 1 ? 's' : ''}
        </button>
      </div>
      <div class="comments-section hidden" id="comments-${post.id}">
        ${commentsHTML}
        <div class="add-comment">
          <input type="text" placeholder="Write a comment..." id="comment-input-${post.id}" />
          <button data-post-id="${post.id}" class="submit-comment-btn">Send</button>
        </div>
      </div>
    </div>
  `;
}

// ── Render feed ──────────────────────────────────────────
function renderFeed() {
  const feed = document.getElementById('feed');
  const empty = document.getElementById('feedEmpty');
  if (network.posts.length === 0) {
    empty.classList.remove('hidden');
    feed.innerHTML = '';
    feed.appendChild(empty);
    return;
  }
  empty.classList.add('hidden');
  feed.innerHTML = network.posts.map(renderPost).join('');

  // Like buttons
  feed.querySelectorAll('.like-btn').forEach(btn => {
    btn.addEventListener('click', () => handleLike(parseInt(btn.dataset.postId)));
  });

  // Toggle comments
  feed.querySelectorAll('.comment-toggle-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const sec = document.getElementById('comments-' + btn.dataset.postId);
      sec.classList.toggle('hidden');
    });
  });

  // Submit comments
  feed.querySelectorAll('.submit-comment-btn').forEach(btn => {
    btn.addEventListener('click', () => handleComment(parseInt(btn.dataset.postId)));
  });
  feed.querySelectorAll('[id^="comment-input-"]').forEach(input => {
    input.addEventListener('keydown', e => {
      if (e.key === 'Enter') {
        const postId = parseInt(input.id.replace('comment-input-', ''));
        handleComment(postId);
      }
    });
  });

  // Poll votes
  feed.querySelectorAll('input[type="radio"]').forEach(radio => {
    radio.addEventListener('change', () => {
      const match = radio.name.match(/poll-(\d+)/);
      if (!match) return;
      const postId = parseInt(match[1]);
      const post = network.posts.find(p => p.id === postId);
      if (post instanceof PollPost) {
        post.vote(activeUser, parseInt(radio.value));
        refreshPostCard(post);
      }
    });
  });
}

// ── Refresh a single post card in place ─────────────────
function refreshPostCard(post) {
  const card = document.getElementById('post-card-' + post.id);
  if (!card) return;
  const openComments = !document.getElementById('comments-' + post.id)?.classList.contains('hidden');
  card.outerHTML = renderPost(post);
  // re-attach listeners on this card only (simpler: full re-render)
  renderFeed();
  if (openComments) {
    document.getElementById('comments-' + post.id)?.classList.remove('hidden');
  }
}

// ── Render notifications badge ───────────────────────────
function renderNotifBadge() {
  const badge = document.getElementById('notifBadge');
  const count = activeUser.unreadCount;
  badge.textContent = count;
  badge.style.display = count === 0 ? 'none' : 'flex';
}

// ── Render notifications panel ───────────────────────────
function renderNotifPanel() {
  const list = document.getElementById('notifList');
  const notifs = activeUser.notifications;
  if (notifs.length === 0) {
    list.innerHTML = `<div class="notif-empty">No notifications yet</div>`;
    return;
  }
  list.innerHTML = notifs.map(n => `
    <div class="notif-item ${n.read ? '' : 'unread'}" data-notif-id="${n.id}">
      <div class="notif-avatar" style="background:${n.sender.color}">${n.sender.username[0]}</div>
      <div class="notif-body">
        <div><strong>@${n.sender.username}</strong> ${escHtml(n.content.replace(n.sender.username + ' ', ''))}</div>
        <div class="notif-time">${timeAgo(n.timestamp)}</div>
      </div>
    </div>
  `).join('');

  list.querySelectorAll('.notif-item').forEach(item => {
    item.addEventListener('click', () => {
      const id = parseInt(item.dataset.notifId);
      const notif = activeUser.notifications.find(n => n.id === id);
      if (notif) { notif.read = true; renderNotifBadge(); item.classList.remove('unread'); }
    });
  });
}

// ── Full re-render ───────────────────────────────────────
function renderAll() {
  renderUsers();
  renderFeed();
  renderNotifBadge();
}

// =========================================================
//  ACTIONS
// =========================================================
function handleLike(postId) {
  const post = network.posts.find(p => p.id === postId);
  if (!post) return;
  try {
    if (post.hasLiked(activeUser)) {
      network.unlikePost(post, activeUser);
    } else {
      network.likePost(post, activeUser);
    }
    renderAll();
  } catch (e) {
    // already handled by toggle
  }
}

function handleComment(postId) {
  const post = network.posts.find(p => p.id === postId);
  const input = document.getElementById('comment-input-' + postId);
  if (!post || !input) return;
  const text = input.value.trim();
  if (!text) return;
  network.addComment(activeUser, post, text);
  input.value = '';
  renderAll();
  document.getElementById('comments-' + postId)?.classList.remove('hidden');
}

// =========================================================
//  MODAL – Create Post
// =========================================================
const modal = document.getElementById('createPostModal');
const modalError = document.getElementById('modalError');

document.getElementById('openCreatePost').addEventListener('click', () => {
  modal.classList.remove('hidden');
  modalError.classList.add('hidden');
  document.getElementById('postContent').value = '';
});
document.getElementById('closeModal').addEventListener('click', () => modal.classList.add('hidden'));
modal.addEventListener('click', e => { if (e.target === modal) modal.classList.add('hidden'); });

document.querySelectorAll('.tab-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    currentPostType = btn.dataset.type;
    document.getElementById('eventFields').classList.toggle('hidden', currentPostType !== 'event');
    document.getElementById('pollFields').classList.toggle('hidden', currentPostType !== 'poll');
  });
});

document.getElementById('submitPost').addEventListener('click', () => {
  const content = document.getElementById('postContent').value.trim();
  modalError.classList.add('hidden');

  try {
    if (currentPostType === 'text') {
      network.createPost(activeUser, content);
    } else if (currentPostType === 'event') {
      const dateVal = document.getElementById('eventDate').value;
      const location = document.getElementById('eventLocation').value.trim() || 'TBD';
      const evDate = dateVal ? new Date(dateVal) : new Date();
      network.createEventPost(activeUser, content, evDate, location);
    } else if (currentPostType === 'poll') {
      const opts = [...document.querySelectorAll('.poll-opt')].map(i => i.value.trim()).filter(Boolean);
      network.createPollPost(activeUser, content, opts);
    }
    modal.classList.add('hidden');
    renderAll();
  } catch (e) {
    modalError.textContent = e.message;
    modalError.classList.remove('hidden');
  }
});

// =========================================================
//  NOTIFICATIONS PANEL
// =========================================================
const notifPanel = document.getElementById('notifPanel');

document.getElementById('notifBell').addEventListener('click', () => {
  notifPanel.classList.toggle('hidden');
  if (!notifPanel.classList.contains('hidden')) renderNotifPanel();
});
document.getElementById('closeNotifPanel').addEventListener('click', () => notifPanel.classList.add('hidden'));

// =========================================================
//  SWITCH ACTIVE USER (header select)
// =========================================================
document.getElementById('activeUserSelect').addEventListener('change', e => {
  activeUser = network.findUserByUsername(e.target.value);
  document.getElementById('activeUserLabel').textContent = activeUser.username;
  notifPanel.classList.add('hidden');
  renderAll();
});

// =========================================================
//  UTILITY
// =========================================================
function escHtml(str) {
  return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

// =========================================================
//  INIT
// =========================================================
renderAll();
