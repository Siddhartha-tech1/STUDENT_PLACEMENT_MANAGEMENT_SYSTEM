import { Check, ChevronDown, Search } from 'lucide-react';
import { useEffect, useMemo, useRef, useState } from 'react';

function normalizeGroups(options = []) {
  if (!options.length) return [];
  if (options[0]?.options) return options;
  return [{ label: null, options }];
}

function flatOptions(groups) {
  return groups.flatMap((group) => group.options.map((option) => ({ ...option, group: group.label })));
}

export default function Dropdown({
  label,
  options = [],
  value,
  onChange,
  placeholder = 'Select an option',
  defaultOption,
  disabled = false,
  className = '',
  persistKey
}) {
  const buttonRef = useRef(null);
  const listRef = useRef(null);
  const searchRef = useRef(null);
  const [open, setOpen] = useState(false);
  const [query, setQuery] = useState('');
  const [activeIndex, setActiveIndex] = useState(0);
  const typeBuffer = useRef('');
  const typeTimer = useRef(null);

  const groups = useMemo(() => normalizeGroups(defaultOption ? [{ label: null, options: [defaultOption] }, ...normalizeGroups(options)] : options), [options, defaultOption]);
  const flattened = useMemo(() => flatOptions(groups), [groups]);
  const searchable = flattened.length > 7;
  const selected = flattened.find((option) => String(option.value) === String(value));
  const filteredGroups = useMemo(() => {
    const needle = query.trim().toLowerCase();
    if (!needle) return groups;
    return groups
      .map((group) => ({
        ...group,
        options: group.options.filter((option) =>
          `${option.label || option.value} ${option.description || ''}`.toLowerCase().includes(needle)
        )
      }))
      .filter((group) => group.options.length);
  }, [groups, query]);
  const visible = useMemo(() => flatOptions(filteredGroups), [filteredGroups]);

  useEffect(() => {
    if (!persistKey || value === undefined || value === null || value === '') return;
    localStorage.setItem(persistKey, String(value));
  }, [persistKey, value]);

  useEffect(() => {
    const handler = (event) => {
      if (!buttonRef.current?.contains(event.target) && !listRef.current?.contains(event.target)) {
        setOpen(false);
      }
    };
    document.addEventListener('pointerdown', handler);
    return () => document.removeEventListener('pointerdown', handler);
  }, []);

  useEffect(() => {
    if (open && searchable) setTimeout(() => searchRef.current?.focus(), 0);
  }, [open, searchable]);

  const choose = (option) => {
    if (!option || option.disabled) return;
    onChange?.(option.value, option);
    setOpen(false);
    setQuery('');
    buttonRef.current?.focus();
  };

  const move = (direction) => {
    if (!visible.length) return;
    let next = activeIndex;
    for (let count = 0; count < visible.length; count += 1) {
      next = (next + direction + visible.length) % visible.length;
      if (!visible[next]?.disabled) break;
    }
    setActiveIndex(next);
  };

  const onKeyDown = (event) => {
    if (disabled) return;
    if (!open && ['ArrowDown', 'ArrowUp', 'Enter', ' '].includes(event.key)) {
      event.preventDefault();
      setOpen(true);
      return;
    }
    if (!open) return;
    if (event.key === 'Escape') {
      setOpen(false);
      buttonRef.current?.focus();
    } else if (event.key === 'ArrowDown') {
      event.preventDefault();
      move(1);
    } else if (event.key === 'ArrowUp') {
      event.preventDefault();
      move(-1);
    } else if (event.key === 'Enter') {
      event.preventDefault();
      choose(visible[activeIndex]);
    } else if (!searchable && event.key.length === 1) {
      typeBuffer.current += event.key.toLowerCase();
      clearTimeout(typeTimer.current);
      typeTimer.current = setTimeout(() => { typeBuffer.current = ''; }, 700);
      const match = visible.findIndex((option) => !option.disabled && String(option.label || option.value).toLowerCase().startsWith(typeBuffer.current));
      if (match >= 0) setActiveIndex(match);
    }
  };

  return (
    <div className={`dropdown-field ${className}`}>
      {label ? <span className="dropdown-label">{label}</span> : null}
      <button
        type="button"
        ref={buttonRef}
        className="dropdown-trigger"
        aria-haspopup="listbox"
        aria-expanded={open}
        disabled={disabled}
        onClick={() => setOpen((current) => !current)}
        onKeyDown={onKeyDown}
      >
        <span className={selected ? 'dropdown-value' : 'dropdown-placeholder'}>{selected?.label || placeholder}</span>
        <ChevronDown size={18} className={open ? 'dropdown-chevron open' : 'dropdown-chevron'} />
      </button>
      {open ? (
        <div className="dropdown-menu-panel" ref={listRef} onKeyDown={onKeyDown}>
          {searchable ? (
            <label className="dropdown-search">
              <Search size={16} />
              <input ref={searchRef} value={query} onChange={(event) => { setQuery(event.target.value); setActiveIndex(0); }} placeholder="Search options" />
            </label>
          ) : null}
          <div role="listbox" tabIndex={-1} className="dropdown-list" aria-label={label || placeholder}>
            {filteredGroups.map((group) => (
              <div key={group.label || 'default'}>
                {group.label ? <div className="dropdown-group">{group.label}</div> : null}
                {group.options.map((option) => {
                  const index = visible.findIndex((item) => item.value === option.value);
                  const isSelected = String(option.value) === String(value);
                  const isActive = index === activeIndex;
                  const Icon = option.icon;
                  return (
                    <button
                      type="button"
                      role="option"
                      aria-selected={isSelected}
                      aria-disabled={option.disabled || undefined}
                      key={`${group.label || 'group'}-${option.value}`}
                      className={`dropdown-option ${isSelected ? 'selected' : ''} ${isActive ? 'active' : ''}`}
                      disabled={option.disabled}
                      onMouseEnter={() => setActiveIndex(index)}
                      onClick={() => choose(option)}
                    >
                      {Icon ? <Icon size={18} /> : null}
                      <span>
                        <strong>{option.label || option.value}</strong>
                        {option.description ? <small>{option.description}</small> : null}
                      </span>
                      {isSelected ? <Check size={17} className="dropdown-check" /> : null}
                    </button>
                  );
                })}
              </div>
            ))}
            {!visible.length ? <div className="dropdown-empty">No options found</div> : null}
          </div>
        </div>
      ) : null}
    </div>
  );
}
